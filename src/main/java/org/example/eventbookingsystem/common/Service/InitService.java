package org.example.eventbookingsystem.common.Service;

import jakarta.annotation.PostConstruct;
import org.example.eventbookingsystem.common.repository.EventRepository;
import org.example.eventbookingsystem.domain.entity.Address;
import org.example.eventbookingsystem.domain.entity.Event;
import org.example.eventbookingsystem.security.repository.UserRepository;
import org.example.eventbookingsystem.security.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
public class InitService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public InitService(PasswordEncoder passwordEncoder, UserRepository userRepository, EventRepository eventRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    private void init() {
        User adminUser = new User();
        adminUser.setUsername("seha");
        adminUser.setPassword(passwordEncoder.encode("seha1234"));
        adminUser.setRoles(Set.of("ADMIN"));
        adminUser.setEnabled(true);

        if (!userRepository.existsByUsername("seha")) {
            adminUser = userRepository.save(adminUser);
        } else {
            adminUser = userRepository.findByUsername("seha").orElse(adminUser);
        }

        User user2 = new User();
        user2.setUsername("ali");
        user2.setPassword(passwordEncoder.encode("ali12345"));
        user2.setRoles(Set.of("USER"));
        user2.setEnabled(true);

        if (!userRepository.existsByUsername("ali")) {
            userRepository.save(user2);
        }

        if (eventRepository.count() == 0) {
            createSampleEvents(adminUser);
        }
    }

    private void createSampleEvents(User adminUser) {
        Event techConference = new Event();
        techConference.setTitle("Tech Conference 2025");
        techConference.setDescription("Join us for the biggest tech conference of the year. Learn about the latest technologies and network with industry professionals.");
        techConference.setCapacity(500L);

        OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
        OffsetDateTime startTime = now.plus(2, ChronoUnit.MONTHS).truncatedTo(ChronoUnit.HOURS).withHour(9);
        OffsetDateTime endTime = startTime.plus(2, ChronoUnit.DAYS).withHour(18);

        techConference.setStartTime(startTime);
        techConference.setEndTime(endTime);

        Address techAddress = new Address();
        techAddress.setStreet("123 Tech Blvd");
        techAddress.setCity("San Francisco");
        techAddress.setCountry("USA");
        techAddress.setPostcode("94105");

        techConference.setAddress(techAddress);
        techConference.setUser(adminUser);

        eventRepository.save(techConference);

        Event musicFestival = new Event();
        musicFestival.setTitle("Summer Music Festival");
        musicFestival.setDescription("A three-day music festival featuring top artists from around the world. Food, drinks, and camping available.");
        musicFestival.setCapacity(10000L);

        OffsetDateTime festivalStart = now.plus(3, ChronoUnit.MONTHS).truncatedTo(ChronoUnit.HOURS).withHour(12);
        OffsetDateTime festivalEnd = festivalStart.plus(3, ChronoUnit.DAYS).withHour(23);

        musicFestival.setStartTime(festivalStart);
        musicFestival.setEndTime(festivalEnd);

        Address festivalAddress = new Address();
        festivalAddress.setStreet("456 Festival Grounds");
        festivalAddress.setCity("Los Angeles");
        festivalAddress.setCountry("USA");
        festivalAddress.setPostcode("90001");

        musicFestival.setAddress(festivalAddress);
        musicFestival.setUser(adminUser);

        eventRepository.save(musicFestival);

        Event businessWorkshop = new Event();
        businessWorkshop.setTitle("Entrepreneurship Workshop");
        businessWorkshop.setDescription("Learn how to start and grow your business from successful entrepreneurs. Networking opportunities available.");
        businessWorkshop.setCapacity(100L);

        OffsetDateTime workshopStart = now.plus(1, ChronoUnit.MONTHS).truncatedTo(ChronoUnit.HOURS).withHour(10);
        OffsetDateTime workshopEnd = workshopStart.plus(8, ChronoUnit.HOURS);

        businessWorkshop.setStartTime(workshopStart);
        businessWorkshop.setEndTime(workshopEnd);

        Address workshopAddress = new Address();
        workshopAddress.setStreet("789 Business Center");
        workshopAddress.setCity("New York");
        workshopAddress.setCountry("USA");
        workshopAddress.setPostcode("10001");

        businessWorkshop.setAddress(workshopAddress);
        businessWorkshop.setUser(adminUser);

        eventRepository.save(businessWorkshop);
    }
}
