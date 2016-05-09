package com.controller;
import com.content.*;
import com.model.StudentRepository;
import com.model.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class Controller {
	
	@Autowired
	private TimeTableRepository repository;
	@Autowired
	private StudentRepository studentrepository;

	@RequestMapping(value="/signout", method=RequestMethod.POST)
	public ResponseEntity<String> logout(@CookieValue(value="sid",defaultValue="888888") String sid,HttpServletRequest request) {
		System.out.println("sign out sid: "+sid);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Set-Cookie","sid="+sid+"; max-age=0");
		return new ResponseEntity<>("Clean Cookie", headers,HttpStatus.OK);
		/*	for(Cookie cookie : request.getCookies()) {
		if(cookie.getName() == "sid") {
				//Clear one cookie
				cookie.setName("");
				cookie.setMaxAge(0);
				cookie.setPath(request.getContextPath());
				response.addCookie(cookie);
				//Clear the other cookie
				Cookie cookieWithSlash = cookie.clone();
				cookieWithSlash.setPath(request.getContextPath() + "/");
				response.addCookie(cookieWithSlash);
			}
		}*/
	}


	@RequestMapping(value="/studentall", method=RequestMethod.GET)
	public ResponseEntity<List<Student>> getAllStudent() {
		List<Student> listStudent = studentrepository.findAll();
    	if (listStudent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(listStudent, HttpStatus.OK);
	}

	@RequestMapping(value="/admin", method=RequestMethod.POST)
	public ResponseEntity<Long> setMaxNum(@RequestParam("maxNum") int maxNum ) {
		List<Student> listStudent = studentrepository.findAll();
    	if (listStudent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    	for(int i = 0; i < listStudent.size(); i++) {
    		if(!listStudent.get(i).getStudentName().equals("admin")) {
    			listStudent.get(i).setMaxNum(maxNum);
        		listStudent.get(i).setRole("1");
    		} else {
    			listStudent.get(i).setMaxNum(Integer.MAX_VALUE);
        		listStudent.get(i).setRole("0");
    		}
    	}
    	studentrepository.save(listStudent);
        return new ResponseEntity<>(HttpStatus.OK);
	}	
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ResponseEntity<Student> getStudentById(@RequestParam(value = "id", required = true) String id,
												@RequestParam(value = "password", required = true) String password,HttpServletResponse response) {
		Student s = studentrepository.findById(id);
    	if (s == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    	if(s.getPassword().equals(password)) {
    		//HttpHeaders headers = new HttpHeaders();
        	//headers.add("Set-Cookie","sid="+id+"; path=/;max-age=3600;");
			Cookie c_id = new Cookie("sid",id); //
			c_id.setMaxAge(1000); //set expire time to 1000 sec

			response.addCookie(c_id); //put cookie in response
			return new ResponseEntity<>(s, HttpStatus.OK);

			//return new ResponseEntity<>(s,headers, HttpStatus.OK);
    	}
        return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value="/student", method=RequestMethod.GET)
	public ResponseEntity<Student> findStudentById(@CookieValue(value="sid", defaultValue="123456") String sid) {
		Student s = studentrepository.findById(sid);

		if(s == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(s, HttpStatus.OK);

	}
	@RequestMapping(value="/student/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Long> delStudentById(@PathVariable("id") String id ) {
		Long l = studentrepository.deleteById(id);
    	if (l == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(l, HttpStatus.OK);
	}	
	
	@RequestMapping(value="/registration", method=RequestMethod.POST)
	public ResponseEntity<Student> createStudentInfo(@RequestBody Student input) {
		Student s = studentrepository.findById(input.getId());
		if(s != null) {
			s.setPassword(input.getPassword());
			s.setStudentName(input.getStudentName());
			s.setMaxNum(input.getMaxNum());
			studentrepository.save(s);
		} else {
			s = new Student(input.getId(), 0, input.getMaxNum(), input.getStudentName(), input.getPassword());
			if(input.getStudentName().equals("admin")) {
				s.setRole("0");
				s.setMaxNum(Integer.MAX_VALUE);
			} else {
				s.setRole("1");
			}
			studentrepository.save(s);
		}
    	
        return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/timetable/{date}", method=RequestMethod.GET)
	public ResponseEntity<TimeTable> getResvInfoByDate(@PathVariable("date") String date) {
		TimeTable tt = repository.findByDate(date);
    	if (tt == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tt, HttpStatus.OK);
	}

	@RequestMapping(value="/timetable", method=RequestMethod.GET)
	public ResponseEntity<List<TimeTable>> getAllResvInfo() {
		List<TimeTable> tt = repository.findAll();
    	if (tt == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tt, HttpStatus.OK);
	}

	
	@RequestMapping(value="/book", method=RequestMethod.POST)
	public ResponseEntity<TimeTable> resvRoom(@CookieValue(value="sid", defaultValue="888888") String sid, @RequestBody StudentResv input) {
		//check student status
		System.out.println("book sid: " + sid);
		Student s = studentrepository.findById(sid);
		if(s == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();        
		String curDate = df.format(today);
		Calendar cal = Calendar.getInstance();
		int curSlot = cal.get(Calendar.HOUR_OF_DAY);
		
		if(input.getDate().compareTo(curDate) < 0 
		|| (input.getDate().compareTo(curDate) == 0 && (input.getSlot()+8) <= curSlot)) {
			return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}

		for(int i = 0; i < s.getReservation().size(); i++) {
			StudentResv studentresv = s.getReservation().get(i);
			if(studentresv.getDate().compareTo(curDate) < 0) {
				s.getReservation().remove(i);
				continue;
			}
			if(studentresv.getDate().compareTo(curDate) == 0 && (studentresv.getSlot()+8) < curSlot) {
				s.getReservation().remove(i);
				continue;
			}
		}
		s.setCurNum(s.getReservation().size());
		if(s.getRole().equals("1") && s.getCurNum() >= s.getMaxNum()) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		s.getReservation().add(input);
		s.setCurNum(s.getCurNum()+1);
		
		//check room status
		TimeTable tt = repository.findByDate(input.getDate());
		if (tt == null) {
			tt = new TimeTable(input.getDate(), sid, input.getRoomid(), input.getSlot());
			repository.save(tt);
			studentrepository.save(s);
			return new ResponseEntity<>(HttpStatus.CREATED);
        }
		for(ResvInfo r : tt.getTimesheet()) {
			if(r.getRoomNumber().equals(input.getRoomid())) {
				for(TimeSlot ts : r.getTimeSlots()) {
					if(ts.getTimeSlot() == input.getSlot()) {
						return new ResponseEntity<>(HttpStatus.CONFLICT);
					}
				}
				r.getTimeSlots().add(new TimeSlot(sid,input.getSlot()));
				repository.save(tt);
				studentrepository.save(s);
				return new ResponseEntity<>(HttpStatus.CREATED);
			}
		}
		tt.getTimesheet().add(new ResvInfo(sid, input.getRoomid(), input.getSlot()));
		repository.save(tt);
		studentrepository.save(s);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/book", method=RequestMethod.DELETE)
	public ResponseEntity<String> cancleRoom(@CookieValue(value="sid", defaultValue="123456") String sid, @RequestBody StudentResv input) {
		System.out.println("cancel book sid: "+sid);
		return cancelRoom(sid, input);
	}
	
	@RequestMapping(value="/admin", method=RequestMethod.DELETE)
	public ResponseEntity<String> cancleRoomByAdmin(@RequestParam("sid") String sid, @RequestBody StudentResv input) {
		return cancelRoom(sid, input);
	}
	
	private ResponseEntity<String> cancelRoom(String sid, StudentResv input) {
		//check student status
		Student s = studentrepository.findById(sid);
		if(s == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		for(int i = 0; i < s.getReservation().size(); i++) {
			StudentResv studentresv = s.getReservation().get(i);
			if(studentresv.getDate().equals(input.getDate())
			&& studentresv.getRoomid().equals(input.getRoomid())
			&& studentresv.getSlot() == input.getSlot()) {
				s.getReservation().remove(i);
				s.setCurNum(s.getCurNum()-1);
				break;
			}
		}
		//check room status
		TimeTable tt = repository.findByDate(input.getDate());
		if (tt == null) {
			return new ResponseEntity<>(HttpStatus.CREATED);
        }

		for(ResvInfo r : tt.getTimesheet()) {
			if(r.getRoomNumber().equals(input.getRoomid())) {
				for(int i = 0; i <  r.getTimeSlots().size(); i++) {
					if(r.getTimeSlots().get(i).getTimeSlot() == input.getSlot()) {
						r.getTimeSlots().remove(i);
					}
				}
			}
		}
		
		repository.save(tt);
		studentrepository.save(s);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
