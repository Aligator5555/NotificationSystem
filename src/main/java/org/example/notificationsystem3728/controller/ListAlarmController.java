package org.example.notificationsystem3728.controller;

import org.example.notificationsystem3728.repository.ListSubscribersRepository;
import org.example.notificationsystem3728.repository.SignalRepository;
import org.example.notificationsystem3728.service.ListAlarmService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ListAlarmController {

    private final SignalRepository signalRepository;
    private final ListAlarmService listAlarmService;
    private  final ListSubscribersRepository listSubscribersRepository;

    public ListAlarmController(SignalRepository signalRepository,
                               ListAlarmService listAlarmService, ListSubscribersRepository listSubscribersRepository) {
        this.signalRepository = signalRepository;
        this.listAlarmService = listAlarmService;
        this.listSubscribersRepository = listSubscribersRepository;
    }

    @GetMapping("/saveAlarm")
    public String showAlarmsPage(Model model) {
        model.addAttribute("signals", signalRepository.findAll());
        model.addAttribute("allSubscribers", listSubscribersRepository.findAll());
        return "saveAlarm";
    }

    @PostMapping("/saveAlarm")
    public String saveAlarm(@RequestParam String nameAlarm,
                            @RequestParam Long nameSignal,
                            @RequestParam(required = false) String descriptionAlarm,
                            Model model) {

        listAlarmService.saveListAlarm(nameAlarm,nameSignal,descriptionAlarm);
        return "redirect:/list";
    }

    @GetMapping("/deleteAlarm/{id}")
    public String deleteAlarm(@PathVariable Long id){
        listAlarmService.deleteAlarm(id);
        return "redirect:/";
    }
}

