package pl.akademiaspring.week2.api;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Api {

    Map<String, Integer> users;

    public Api() {
        users = new HashMap();
        users.put("user", 0);
        users.put("admin", 0);
    }

    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent successEvent) {
        String user = ((User)successEvent.getAuthentication().getPrincipal()).getUsername();
        int count;

        switch(user.toLowerCase()) {
            case "user":
                count = users.get("user");
                users.put("user", ++count);
                break;
            case "admin":
                count = users.get("admin");
                users.put("admin", ++count);
                break;
            default:
                break;
        }
    }

    @GetMapping("/bye")
    public String bye() {
        return "pa pa";
    }

    @GetMapping("/helloAdmin")
    public String helloAdmin(Principal principal) {
        return "Cześć Admin: " + principal.getName() +
                ", zalogowałeś się : " + users.get(principal.getName()) + " raz";
    }

    @GetMapping("/helloUser")
    public String helloUser(Principal principal) {

        return "Cześć User: " + principal.getName() +
                ", zalogowałeś się : " + users.get(principal.getName()) + " raz";
    }

    @GetMapping("/helloAnonymous")
    public String helloAnonymous(Principal principal) {

        if (principal == null) {
            return "Cześć : Nieznajomy";
        }
        return "Cześć Anonymous: " + principal.getName();
    }
}
