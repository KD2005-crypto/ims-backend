package com.codeb.ims;

import com.codeb.ims.entity.ChartData;
import com.codeb.ims.entity.Member;
import com.codeb.ims.entity.Project;
import com.codeb.ims.entity.User;
import com.codeb.ims.repository.ChartDataRepository;
import com.codeb.ims.repository.MemberRepository;
import com.codeb.ims.repository.ProjectRepository;
import com.codeb.ims.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired private ProjectRepository projectRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ChartDataRepository chartRepository;
    @Autowired private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 1. SEED ADMIN USER (✅ FIXED: Using setFullName)
        if (userRepository.count() == 0) {
            System.out.println("👤 Creating Admin User...");
            User admin = new User();

            // This was the error! We changed it to setFullName:
            admin.setFullName("Admin User");

            admin.setEmail("admin@codeb.com");
            admin.setPassword("admin123");
            admin.setRole("ADMIN");
            admin.setStatus("active");

            userRepository.save(admin);
            System.out.println("✅ Default Admin Created: admin@codeb.com / admin123");
        }

        // 2. SEED PROJECTS (Untouched)
        if (projectRepository.count() == 0) {
            System.out.println("🌱 Seeding Project Data...");
            Member m1 = new Member(); m1.setName("Ryan Tompson"); m1.setAvatarPath("/team-1.jpg");
            Member m2 = new Member(); m2.setName("Romina Hadid"); m2.setAvatarPath("/team-2.jpg");
            Member m3 = new Member(); m3.setName("Alexander Smith"); m3.setAvatarPath("/team-3.jpg");
            Member m4 = new Member(); m4.setName("Jessica Doe"); m4.setAvatarPath("/team-4.jpg");

            List<Member> saved = memberRepository.saveAll(Arrays.asList(m1, m2, m3, m4));
            m1=saved.get(0); m2=saved.get(1); m3=saved.get(2); m4=saved.get(3);

            createProject("Material UI XD Version", "$14,000", 60, "/logo-xd.svg", Arrays.asList(m1, m2, m3, m4));
            createProject("Add Progress Track", "$3,000", 10, "/logo-atlassian.svg", Arrays.asList(m2, m4));
            createProject("Fix Platform Errors", "Not set", 100, "/logo-slack.svg", Arrays.asList(m1, m3));
            createProject("Launch our Mobile App", "$20,500", 100, "/logo-spotify.svg", Arrays.asList(m4, m3, m1));
        }

        // 3. SEED CHARTS (Untouched)
        if (chartRepository.count() == 0) {
            System.out.println("📊 Seeding Chart Data...");
            createChart("sales", "Weekly Sales", "M,T,W,T,F,S,S", "50,20,10,22,50,10,40");
            createChart("growth", "Monthly Growth", "Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec", "50,40,300,320,500,350,200,230,500");
            createChart("tasks", "Active Locations", "Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec", "30,40,45,50,40,60,70,90,100");
        }
    }

    private void createProject(String name, String budget, int completion, String logo, List<Member> team) {
        Project p = new Project();
        p.setName(name);
        p.setBudget(budget);
        p.setCompletion(completion);
        p.setLogoPath(logo);
        p.setMembers(team);
        projectRepository.save(p);
    }

    private void createChart(String name, String desc, String labels, String data) {
        ChartData c = new ChartData();
        c.setName(name);
        c.setDescription(desc);
        c.setLabels(labels);
        c.setDataPoints(data);
        c.setUpdateTime("just updated");
        chartRepository.save(c);
    }
}