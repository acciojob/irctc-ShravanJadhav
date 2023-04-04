package com.driver.services;

import com.driver.EntryDto.AddTrainEntryDto;
import com.driver.EntryDto.SeatAvailabilityEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Station;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainService {

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    TicketRepository ticketRepository;
    public Integer addTrain(AddTrainEntryDto trainEntryDto){

        //Add the train to the trainRepository
        //and route String logic to be taken from the Problem statement.
        //Save the train and return the trainId that is generated from the database.
        //Avoid using the lombok library

        //create train object
        Train train = new Train();

        //convert trainEntryDto to train
        //station list converted into String;
        List<Station> stationList = trainEntryDto.getStationRoute();
        StringBuilder route = new StringBuilder();
        int stationSize = stationList.size();
        for(int i=0; i<stationList.size(); i++){
            if(i==stationList.size()-1)
                route.append(stationList.get(i));
            else {
                route.append(stationList.get(i));
                route.append(",");
            }
        }

        train.setRoute(route.toString());
        train.setNoOfSeats(trainEntryDto.getNoOfSeats());
        train.setDepartureTime(trainEntryDto.getDepartureTime());

        trainRepository.save(train);

        return train.getTrainId();

    }

    public Integer calculateAvailableSeats(SeatAvailabilityEntryDto seatAvailabilityEntryDto){

        //Calculate the total seats available
        //Suppose the route is A B C D
        //And there are 2 seats avaialble in total in the train
        //and 2 tickets are booked from A to C and B to D.
        //The seat is available only between A to C and A to B. If a seat is empty between 2 station it will be counted to our final ans
        //even if that seat is booked post the destStation or before the boardingStation
        //Inshort : a train has totalNo of seats and there are tickets from and to different locations
        //We need to find out the available seats between the given 2 stations.

        int tarinId=  seatAvailabilityEntryDto.getTrainId();
        Station fromStation = seatAvailabilityEntryDto.getFromStation();
        Station toStation = seatAvailabilityEntryDto.getToStation();

        Train train = trainRepository.findById(tarinId).get();
        List<Ticket>ticketList = train.getBookedTickets();

        int bookSeats = 0;
        if(ticketList!=null){
            for(Ticket ticket : ticketList){
                if(ticket.getFromStation().equals(fromStation) && ticket.getToStation().equals(toStation)){
                 bookSeats+=ticket.getPassengersList().size();
                }
            }
        }
        int available = train.getNoOfSeats()-bookSeats;

        return available;

    }

    public Integer calculatePeopleBoardingAtAStation(Integer trainId,Station station) throws Exception{

        //We need to find out the number of people who will be boarding a train from a particular station
        //if the trainId is not passing through that station
        //throw new Exception("Train is not passing from this station");
        //  in a happy case we need to find out the number of such people.

        Train train = trainRepository.findById(trainId).get();
        int totalPeople=0;

        List<Ticket>ticketList = train.getBookedTickets();
        for(Ticket ticket:ticketList){
            if(ticket.getFromStation()==station){
                totalPeople+=ticket.getPassengersList().size();
            }
        }

        if(totalPeople==0){
            throw new Exception("Train is not passing from this station");
        }else
            return totalPeople;
    }

    public Integer calculateOldestPersonTravelling(Integer trainId){

        //Throughout the journey of the train between any 2 stations
        //We need to find out the age of the oldest person that is travelling the train
        //If there are no people travelling in that train you can return 0

        int oldestPerson = 0;
        Train train = trainRepository.findById(trainId).get();
        List<Ticket>ticketList = train.getBookedTickets();

        for(Ticket ticket : ticketList){
            List<Passenger>pList = ticket.getPassengersList();
            for(Passenger p : pList){
                if(p.getAge()>=oldestPerson)
                    oldestPerson=p.getAge();
            }
        }

        return oldestPerson;
    }

    public List<Integer> trainsBetweenAGivenTime(Station station, LocalTime startTime, LocalTime endTime){

        //When you are at a particular station you need to find out the number of trains that will pass through a given station
        //between a particular time frame both start time and end time included.
        //You can assume that the date change doesn't need to be done ie the travel will certainly happen with the same date (More details
        //in problem statement)
        //You can also assume the seconds and milli seconds value will be 0 in a LocalTime format.

         List<Integer> trainList = new ArrayList<>();
         List<Train> trains = trainRepository.findAll();

         for(Train t : trains){
             String s = t.getRoute();
             String[]ans = s.split(",");


         }

         return trainList;
    }

}
