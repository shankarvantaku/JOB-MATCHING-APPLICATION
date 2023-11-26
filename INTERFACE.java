import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceModule {

    private final JobRepository jobRepository;

    @Autowired
    public InterfaceModule(JobRepository jobRepository) {
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
