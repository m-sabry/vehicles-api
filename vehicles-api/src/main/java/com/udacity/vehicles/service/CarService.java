package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.tools.ant.types.selectors.SelectSelector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.text.html.Option;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository carRepository;
    private final MapsClient mapWebClient;
    private final PriceClient priceWebClient;
    public CarService(CarRepository carRepository ,MapsClient mapWebClient,
                      PriceClient priceWebClient) {
        /**
         * _TODO: Add the Maps and Pricing Web Clients you create
         *   in `VehiclesApiApplication` as arguments and set them here.
         */
        this.mapWebClient = mapWebClient;
        this.priceWebClient = priceWebClient;
        this.carRepository = carRepository;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return carRepository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        System.out.println("CarService: findById"  );
        /**
         *
         * _TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */
        Car car;
        Optional<Car> carOptional = carRepository.findById(id);
        if(carOptional.isPresent()) {
            car = carOptional.get();
        }
        else {
            throw new CarNotFoundException("Vehicle with id: " + id + "doesn't exist!");
        }


        /**
         * _TODO: Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the `id` input'
         * _TODO: Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
        String price = priceWebClient.getPrice(id);
        car.setPrice(price);

        /**
         * _TODO: Use the Maps Web client you create in `VehiclesApiApplication`
         *   to get the address for the vehicle. You should access the location
         *   from the car object and feed it to the Maps service.
         *
         * _TODO: Set the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */
        Location carLocation = mapWebClient.getAddress(car.getLocation());
        System.out.println("===== Lat: " + carLocation.getLat() + ", lng" + carLocation.getLon());
        car.setLocation(carLocation);

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return carRepository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setCondition(car.getCondition());
                        carToBeUpdated.setLocation(car.getLocation());
                        carToBeUpdated.setModifiedAt(LocalDateTime.now());
                        return carRepository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return carRepository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         * _TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */
        Car car;
        Optional<Car> carOptional = carRepository.findById(id);
        if(carOptional.isPresent()) {
            car = carOptional.get();
            carRepository.delete(car);
        }
        else {
            throw new CarNotFoundException("Car with id: " + id + "doesn't exist!");
        }

        /**
         * _TODO: Delete the car from the repository.
         */
    }
}
