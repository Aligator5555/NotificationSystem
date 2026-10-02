package org.example.notificationsystem3728.controller;

import org.example.notificationsystem3728.model.ListSubscribers;
import org.example.notificationsystem3728.repository.ListAlarmRepository;
import org.example.notificationsystem3728.repository.ListSubscribersRepository;
import org.example.notificationsystem3728.repository.SignalRepository;
import org.example.notificationsystem3728.service.ListAlarmService;
import org.example.notificationsystem3728.service.ListSubscribersService;
import org.example.notificationsystem3728.service.SignalService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class MainController {
    private final ListSubscribersRepository listSubscribersRepository;
    private final ListSubscribersService listSubscribersService;
    private final SignalRepository signalRepository;
    private final SignalService signalService;
    private final ListAlarmService listAlarmService;
    private final ListAlarmRepository listAlarmRepository;

    public MainController(ListSubscribersRepository listSubscribersRepository, ListSubscribersService listSubscribersService, SignalService signalService, SignalRepository signalRepository, SignalService signalService1, ListAlarmService listAlarmService, ListAlarmRepository listAlarmRepository) {
        this.listSubscribersRepository = listSubscribersRepository;
        this.listSubscribersService = listSubscribersService;
        this.signalRepository = signalRepository;
        this.signalService = signalService1;
        this.listAlarmService = listAlarmService;
        this.listAlarmRepository = listAlarmRepository;
    }

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("alarms", listAlarmRepository.findAll());
        return "main";
    }

   @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("listSubscribers", listSubscribersRepository.findAll());
        model.addAttribute("signals", signalRepository .findAll());
        return "list";
   }
   @GetMapping("/addContact")
   public String addContact() {
        return "addContact";
   }
    // Открыть форму редактирования (GET)
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        ListSubscribers subscriber = listSubscribersService.getProductById(id);
        if (subscriber == null) {
            return "redirect:/contacts/list?error=notFound";
        }
        model.addAttribute("subscriber", subscriber); // имя атрибута должно совпадать с th:object
        return "edit";
    }

    // Сохранить изменения (POST)
    @PostMapping("/edit")
    public String saveEdit(@ModelAttribute ListSubscribers subscriber) {
        listSubscribersService.updateContact(subscriber.getId(), subscriber);
        return "redirect:/list";
    }
   @PostMapping("/saveContact")
    public String saveContact(ListSubscribers listSubscribers) {
        listSubscribersService.saveContact(listSubscribers);
        return "redirect:/list";
   }
   @GetMapping("/deleteContact/{id}")
    public String deleteContact(@PathVariable Long id) {
        listSubscribersService.deleteContact(id);
        return "redirect:/list";
   }
   @PostMapping("/updateContact/{id}")
    public String updateContact(@PathVariable("id") Long id,
                                ListSubscribers listSubscribers){
        listSubscribersService.updateContact(id,listSubscribers);
        return "redirect:/list";

   }

    @PostMapping("/toggle-enabled")
    public String toggleEnabled(@RequestParam Long id) {
        ListSubscribers subscriber = listSubscribersRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        subscriber.setEnabled(!subscriber.isEnabled());
        listSubscribersRepository.save(subscriber);

        return "redirect:/list";
    }


}
