package org.example.notificationsystem3728.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "name_alarms")
public class ListAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // для MySQL лучше IDENTITY
    private Long id;

    private String nameAlarm; // Название тревоги
    private String descriptionAlarm; // Описание

    // Одна тревога — один сигнал
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signal_id", nullable = true)
    private Signal signal;

    // Одна тревога — много подписчиков
    @OneToMany(mappedBy = "alarm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListSubscribers> subscribers = new ArrayList<>();

    public ListAlarm(Long id, String nameAlarm, String descriptionAlarm, Signal signal, List<ListSubscribers> subscribers) {
        this.id = id;
        this.nameAlarm = nameAlarm;
        this.descriptionAlarm = descriptionAlarm;
        this.signal = signal;
        this.subscribers = subscribers;
    }

    public ListAlarm() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAlarm() {
        return nameAlarm;
    }

    public void setNameAlarm(String nameAlarm) {
        this.nameAlarm = nameAlarm;
    }

    public String getDescriptionAlarm() {
        return descriptionAlarm;
    }

    public void setDescriptionAlarm(String descriptionAlarm) {
        this.descriptionAlarm = descriptionAlarm;
    }

    public Signal getSignal() {
        return signal;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }

    public List<ListSubscribers> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<ListSubscribers> subscribers) {
        this.subscribers = subscribers;
    }
}
