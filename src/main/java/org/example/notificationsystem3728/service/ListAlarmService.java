package org.example.notificationsystem3728.service;

import org.example.notificationsystem3728.model.ListAlarm;
import org.example.notificationsystem3728.model.ListSubscribers;
import org.example.notificationsystem3728.model.Signal;
import org.example.notificationsystem3728.repository.ListAlarmRepository;
import org.example.notificationsystem3728.repository.ListSubscribersRepository;
import org.example.notificationsystem3728.repository.SignalRepository;
import org.springframework.stereotype.Service;

@Service
public class ListAlarmService {

    private final ListAlarmRepository listAlarmRepository;
    private final SignalRepository signalRepository;
    private final ListSubscribersRepository listSubscribersRepository;

    public ListAlarmService(ListAlarmRepository listAlarmRepository, SignalRepository signalRepository, ListSubscribersRepository listSubscribersRepository) {
        this.listAlarmRepository = listAlarmRepository;
        this.signalRepository = signalRepository;
        this.listSubscribersRepository = listSubscribersRepository;
    }


    public void saveListAlarm(String nameAlarm,
                              Long signalId,                 // <-- теперь ID (Long), не имя
                              String descriptionAlarm) {

        // Ищем сигнал по ID
        Signal signal = signalRepository.findById(signalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Сигнал с ID " + signalId + " не найден. " +
                                "Выберите существующий сигнал из списка."));

        ListAlarm alarm = new ListAlarm();
        alarm.setNameAlarm(nameAlarm);
        alarm.setDescriptionAlarm(descriptionAlarm);
        alarm.setSignal(signal);
        listAlarmRepository.save(alarm);
    }

    public void deleteAlarm(Long id){
        listAlarmRepository.deleteById(id);
    }
}
