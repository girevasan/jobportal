package com.jobportal.demo;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
	@Autowired 
	private Userrepo userRepo;
	@Autowired
	private Jobsrepo jobRepo;
	@Autowired
	private EncryptionDecryptionrepo edrepo;
    @Autowired
    private JavaMailSender sender;
	
	RSA rsa = new RSA(128);
	Cipher cipher = new Cipher();
	@GetMapping("/")
	public String viewHomePage() {
		return "home";
	}
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
	    model.addAttribute("user", new User());
	     
	    return "register";
	}
	@PostMapping("/process_register")
	public String processRegister(User user) {
		int cpuCost = (int) Math.pow(2, 14); // factor to increase CPU costs
		int memoryCost = 8;      // increases memory usage
		int parallelization = 1; // currently not supported by Spring Security
		int keyLength = 32;      // key length in bytes
		int saltLength = 64;     // salt length in bytes

		SCryptPasswordEncoder PasswordEncoder = new SCryptPasswordEncoder(cpuCost,memoryCost,parallelization,keyLength,saltLength);
		String encodedPassword = PasswordEncoder.encode(user.getPassword());
		
	    user.setPassword(encodedPassword);
	    userRepo.save(user);
	    try {
	    	sendEmail(user.getEmail(),user);
	    }
	    catch(Exception e) {
	    	System.out.println(e);
	    }
	    return "/dashboard";
	}
	@GetMapping("/dashboard")
	public String listUsers(Model model) {
	    return "dashboard";
	}
	@GetMapping("/apply")
	public String showForm(Model model) {
		model.addAttribute("jobs", new Jobs());
		model.addAttribute("ed", new EncryptDecrypt());
		return "apply";
	}
	@PostMapping("/process_apply")
	public String processApply(Jobs jobs,EncryptDecrypt ed) {
    	String msg = jobs.getMobileNo();

    	byte[] crypt = cipher.encrypt(msg, rsa.getPublicKey());
 	   	System.out.println("original text: " + msg);
    	System.out.println("encryption: " + new String(crypt));
    	System.out.println("decryption: " + cipher.decrypt(crypt, rsa.getPrivateKey()));
    	Key k=rsa.getPrivateKey();
    	ed.setPrivateKey(k);
	    jobs.setMobileNo(new String(crypt));
	    edrepo.save(ed);
		jobRepo.save(jobs);
	    try {
	    	sendappliedEmail(jobs.getEmail(),jobs);
	    }
	    catch(Exception e) {
	    	System.out.println(e);
	    }
		return "display";
	}
	@GetMapping("/display")
	public String displayAppliedJobs(Model model) {
		model.addAttribute("jobs", jobRepo.findAll());
		return "display";
	}
	 private void sendEmail(String email,User user) throws Exception{
	        MimeMessage message = sender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	         
	        helper.setTo(email);
	        helper.setText("Welcome to jobs portal.\nHere you will find the jobs well suited for your skills.\n"
	        		+ "Your username is "+user.getEmail());
	        helper.setSubject("Thanks for Registering");
	         
	        sender.send(message);
	    }
	 
	 private void sendappliedEmail(String email,Jobs job) throws Exception{
	        MimeMessage message = sender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	        helper.setTo(email);
	        helper.setText("Welcome to jobs portal.\n"
	        		+ "Your username is "+job.getEmail()
	        		+"\nYou have applied for "+job.getAppliedJob()
	        		);
	        helper.setSubject("Thanks for Applying");
	         
	        sender.send(message);
	    }
}