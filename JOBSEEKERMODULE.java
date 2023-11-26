import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@SpringBootApplication
public class JobSeekerModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobSeekerModuleApplication.class, args);
    }
}

@Entity
class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String requiredSkills;
    private String requiredExperience;
    private String location;

    // Constructors, getters, setters...

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", requiredSkills='" + requiredSkills + '\'' +
                ", requiredExperience='" + requiredExperience + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}

interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByTitleContainingIgnoreCaseOrRequiredSkillsContainingIgnoreCase(String title, String skills);
}

@Service
class JobSeekerService {

    private final JobRepository jobRepository;

    public JobSeekerService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> searchJobs(String searchCriteria) {
        return jobRepository.findByTitleContainingIgnoreCaseOrRequiredSkillsContainingIgnoreCase(searchCriteria, searchCriteria);
    }

    public void applyForJob(Job job) {
        // Logic to apply for a job
        System.out.println("Applied for job:");
        displayJobDetails(job);
    }

    private void displayJobDetails(Job job) {
        // Display job details (customize based on your needs)
        System.out.println("Job ID: " + job.getId());
        System.out.println("Title: " + job.getTitle());
        System.out.println("Required Skills: " + job.getRequiredSkills());
        System.out.println("Required Experience: " + job.getRequiredExperience());
        System.out.println("Location: " + job.getLocation());
        System.out.println("---------------------------");
    }
}

@RestController
@RequestMapping("/jobseeker")
class JobSeekerController {

    private final JobSeekerService jobSeekerService;

    public JobSeekerController(JobSeekerService jobSeekerService) {
        this.jobSeekerService = jobSeekerService;
    }

    @GetMapping("/search")
    public List<Job> searchJobs(@RequestParam String searchCriteria) {
        return jobSeekerService.searchJobs(searchCriteria);
    }

    @PostMapping("/apply")
    public void applyForJob(@RequestBody Job job) {
        jobSeekerService.applyForJob(job);
    }
}
