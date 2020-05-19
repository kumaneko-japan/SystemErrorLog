package syserrLog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SystemErrorLogController {

	@Autowired MessageSource msg;

	@RequestMapping(value = "/validSample", method = RequestMethod.GET)
	public String sample1() {
		return "validSample";
	}

	@RequestMapping(value = "/1", method = RequestMethod.POST)
	public void indexOutOfBoundsException() {
		throw new IndexOutOfBoundsException();
	}

	@RequestMapping(value = "/2", method = RequestMethod.POST)
	public void arithmeticException() {
		throw new ArithmeticException();
	}

	@RequestMapping(value = "/3", method = RequestMethod.POST)
	public void NullPointerException() {
		throw new NullPointerException();
	}
}
