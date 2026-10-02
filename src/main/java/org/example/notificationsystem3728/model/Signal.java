package org.example.notificationsystem3728.model;

import jakarta.persistence.*;

@Entity
@Table(name = "signals")
public class Signal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nameSignal;
    @Column(length = 255)
    private String filePath;
    @Column
    private String mimeType;      // например, audio/wav
    private Integer durationSec;  // длительность в секундах (опционально)

    public Signal() {
    }

    public Signal(Long id, String nameSignal, String filePath, String mimeType, Integer durationSec) {
        this.id = id;
        this.nameSignal = nameSignal;
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.durationSec = durationSec;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameSignal() {
        return nameSignal;
    }

    public void setNameSignal(String nameSignal) {
        this.nameSignal = nameSignal;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(Integer durationSec) {
        this.durationSec = durationSec;
    }
}
