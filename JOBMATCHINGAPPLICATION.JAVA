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
public class JobMatchingApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobMatchingApplication.class, args);
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
class JobPosterService {

    private final JobRepository jobRepository;

    public JobPosterService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public void postJob(Job job) {
        jobRepository.save(job);
        System.out.println("Job posted successfully:");
        displayJobDetails(job);
    }

    public void updateJob(Job job) {
        if (jobRepository.existsById(job.getId())) {
            jobRepository.save(job);
            System.out.println("Job updated successfully:");
            displayJobDetails(job);
        } else {
            System.out.println("Job not found for updating.");
        }
    }

    public void deleteJob(Long jobId) {
        if (jobRepository.existsById(jobId)) {
            jobRepository.deleteById(jobId);
            System.out.println("Job deleted successfully.");
        } else {
            System.out.println("Job not found for deletion.");
        }
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

@Service
class InterfaceModule {

    private final JobPosterService jobPosterService;
    private final JobSeekerService jobSeekerService;

    public InterfaceModule(JobPosterService jobPosterService, JobSeekerService jobSeekerService) {
        this.jobPosterService = jobPosterService;
        this.jobSeekerService = jobSeekerService;
    }

    public void postJob(Job job) {
        jobPosterService.postJob(job);
    }

    public void updateJob(Job job) {
        jobPosterService.updateJob(job);
    }

    public void deleteJob(Long jobId) {
        jobPosterService.deleteJob(jobId);
    }

    public List<Job> searchJobs(String searchCriteria) {
        return jobSeekerService.searchJobs(searchCriteria);
    }

    public void applyForJob(Job job) {
        jobSeekerService.applyForJob(job);
    }
}

@RestController
@RequestMapping("/jobmatching")
class JobMatchingController {

    private final InterfaceModule interfaceModule;

    public JobMatchingController(InterfaceModule interfaceModule) {
        this.interfaceModule = interfaceModule;
    }

    @PostMapping("/postJob")
    public void postJob(@RequestBody Job job) {
        interfaceModule.postJob(job);
    }

    @PutMapping("/updateJob")
    public void updateJob(@RequestBody Job job) {
        interfaceModule.updateJob(job);
    }

    @DeleteMapping("/deleteJob/{jobId}")
    public void deleteJob(@PathVariable Long jobId) {
        interfaceModule.deleteJob(jobId);
    }

    @GetMapping("/searchJobs")
    public List<Job> searchJobs(@RequestParam String searchCriteria) {
        return interfaceModule.searchJobs(searchCriteria);
    }

    @PostMapping("/applyForJob")
    public void applyForJob(@RequestBody Job job) {
        interfaceModule.applyForJob(job);
    }
}
