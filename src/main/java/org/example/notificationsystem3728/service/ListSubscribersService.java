package org.example.notificationsystem3728.service;

import org.example.notificationsystem3728.model.ListSubscribers;
import org.example.notificationsystem3728.repository.ListSubscribersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListSubscribersService {

   private final ListSubscribersRepository listSubscribersRepository;

    public ListSubscribersService(ListSubscribersRepository listSubscribersRepository) {
        this.listSubscribersRepository = listSubscribersRepository;
    }


    public void saveContact(ListSubscribers listSubscribers) {
        listSubscribersRepository.save(listSubscribers);
    }
    @Transactional
    public void deleteContact(Long id) {

        listSubscribersRepository.deleteById(id);
    }

    @Transactional
    public void updateContact(Long contactId,ListSubscribers listSubscribers) {
        ListSubscribers existingListSubscribers = listSubscribersRepository.findById(contactId).orElse(null);

        existingListSubscribers.setDivision(listSubscribers.getDivision());
        existingListSubscribers.setRankValue(listSubscribers.getRankValue());
        existingListSubscribers.setFamily(listSubscribers.getFamily());
        existingListSubscribers.setPhoneNumber(listSubscribers.getPhoneNumber());
        listSubscribersRepository.save(existingListSubscribers);

    }
    public ListSubscribers getProductById(Long id) {

        return listSubscribersRepository.findById(id).orElse(null);
    }

}
