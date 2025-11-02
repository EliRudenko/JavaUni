package com.example.rest_start_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootApplication
public class RestStartTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestStartTestApplication.class, args);
    }
}

@Entity
class DJ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Min(1)
    private int popularityRank;

    public DJ() {}

    public DJ(String name, int popularityRank) {
        this.name = name;
        this.popularityRank = popularityRank;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getPopularityRank() { return popularityRank; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPopularityRank(int popularityRank) { this.popularityRank = popularityRank; }
}

interface DJRepository extends org.springframework.data.jpa.repository.JpaRepository<DJ, Long> {
    boolean existsByPopularityRank(int popularityRank);
}

@Service
class DJService {

    private final DJRepository djRepository;

    public DJService(DJRepository djRepository) {
        this.djRepository = djRepository;
    }

    public List<DJ> getAllDJs() {
        return djRepository.findAll(org.springframework.data.domain.Sort.by("popularityRank"));
    }

    public DJ getDJById(Long id) {
        return djRepository.findById(id).orElseThrow(() -> new NoSuchElementException("dj з id " + id + " не знайдено"));
    }

    public DJ addDJ(DJ dj) {
        if (djRepository.existsByPopularityRank(dj.getPopularityRank())) {
            throw new IllegalArgumentException("рейтинг " + dj.getPopularityRank() + " вже зайнятий");
        }
        return djRepository.save(dj);
    }

    public void deleteDJ(Long id) //!!!!!!!!!!!!!!!!!!!!!!!!!
    {
        if (!djRepository.existsById(id)) {
            throw new NoSuchElementException("dj з id " + id + " не знайдено");
        }
        djRepository.deleteById(id);
    }
}

@RestController
@RequestMapping("/api/djs")
class DJApiController {

    private final DJService djService;

    public DJApiController(DJService djService) {
        this.djService = djService;
    }

    @GetMapping
    public List<DJ> all() {
        return djService.getAllDJs();
    }

    @GetMapping("/{id}")
    public DJ one(@PathVariable Long id) {
        return djService.getDJById(id);
    }

    @PostMapping
    public ResponseEntity<DJ> create(@RequestBody @Valid DJ dj) {
        DJ savedDJ = djService.addDJ(dj);
        return new ResponseEntity<>(savedDJ, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}") // !!!!!!!!!!!!!!!!!!!!!!
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        djService.deleteDJ(id);
        return ResponseEntity.noContent().build();
    }
}

@Controller
@RequestMapping("/")
class WebController {

    private final DJService djService;

    public WebController(DJService djService) {
        this.djService = djService;
    }

    @GetMapping
    public String index(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("djs", djService.getAllDJs());
        return "index";
    }

    @PostMapping("/add")
    public String addDJ(@RequestParam("name") String name,
                        @RequestParam("rank") int rank,
                        RedirectAttributes redirectAttributes) {
        try {
            djService.addDJ(new DJ(name, rank));
            redirectAttributes.addFlashAttribute("success",
                    "Діджея '" + name + "' успішно додано!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/delete") // !!!!!!!!!!!!!!!!!!!!
    public String deleteDJ(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            djService.deleteDJ(id);
            redirectAttributes.addFlashAttribute("success", "Діджей успішно видалений!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }
}

@Component
class BrowserLauncher {
    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowser() {
        System.setProperty("java.awt.headless", "false");
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:8080"));
        } catch (IOException | URISyntaxException ignored) {}
    }
}
