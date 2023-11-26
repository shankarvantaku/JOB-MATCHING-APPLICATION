import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobPosterModule {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public void postJob(Job job) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO jobs (title, required_skills, required_experience, location) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, job.getTitle());
                preparedStatement.setString(2, job.getRequiredSkills());
                preparedStatement.setString(3, job.getRequiredExperience());
                preparedStatement.setString(4, job.getLocation());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            job.setId(String.valueOf(generatedKeys.getLong(1)));
                        }
                    }
                    System.out.println("Job posted successfully:");
                    displayJobDetails(job);
                } else {
                    System.out.println("Failed to post the job.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateJob(Job job) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE jobs SET title=?, required_skills=?, required_experience=?, location=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, job.getTitle());
                preparedStatement.setString(2, job.getRequiredSkills());
                preparedStatement.setString(3, job.getRequiredExperience());
                preparedStatement.setString(4, job.getLocation());
                preparedStatement.setLong(5, Long.parseLong(job.getId()));

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Job updated successfully:");
                    displayJobDetails(job);
                } else {
                    System.out.println("Job not found for updating.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteJob(String jobId) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM jobs WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, Long.parseLong(jobId));

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Job deleted successfully.");
                } else {
                    System.out.println("Job not found for deletion.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM jobs";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Job job = new Job(
                            String.valueOf(resultSet.getLong("id")),
                            resultSet.getString("title"),
                            resultSet.getString("required_skills"),
                            resultSet.getString("required_experience"),
                            resultSet.getString("location")
                    );
                    jobs.add(job);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public Job getJobById(String jobId) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM jobs WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, Long.parseLong(jobId));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return new Job(
                                String.valueOf(resultSet.getLong("id")),
                                resultSet.getString("title"),
                                resultSet.getString("required_skills"),
                                resultSet.getString("required_experience"),
                                resultSet.getString("location")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
