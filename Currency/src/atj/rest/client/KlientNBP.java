package atj.rest.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import atj.rest.server.ExchangeRatesSeries;
import atj.rest.server.Rate;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

@Path("/webapi")
public class KlientNBP {

	ExchangeRatesSeries exrs = new ExchangeRatesSeries();
	List<Rate> syl;

	private double sum;

	private double calculateAverage() {
		ArrayList<Double> aver = new ArrayList<Double>();

		for (Rate field : syl) {
			aver.add(field.getRate());
			System.out.println(field.getRate());
			sum += field.getRate();
		}
		return sum / aver.size();
	}

	public ExchangeRatesSeries unmarshaller(String curParam, String numParam) throws MalformedURLException {
		try {
			JAXBContext jc = JAXBContext.newInstance(ExchangeRatesSeries.class);
			Unmarshaller u = jc.createUnmarshaller();
			URL url = new URL(
					"http://api.nbp.pl/api/exchangerates/rates/a/" + curParam + "/last/" + numParam + "/?format=xml");
			exrs = (ExchangeRatesSeries) u.unmarshal(url);
			System.out.println(" unmarshalled " + exrs.getCode());
			System.out.println(" unmarshalled " + exrs.getRates());
			syl = exrs.getRates();

			return exrs;

		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println("Niepowodzenie");
		}
		return null;
	}

	@GET
	@Path("/{currency}/{lastDays}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public ExchangeRatesSeries getStream(@PathParam("currency") String curParam, @PathParam("lastDays") String numParam)
			throws MalformedURLException {
		return unmarshaller(curParam, numParam);
	}

	@GET
	@Path("/{currency}/{lastDays}/json")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public ExchangeRatesSeries getStreamJSON(@PathParam("currency") String curParam,
			@PathParam("lastDays") String numParam) throws MalformedURLException {
		return unmarshaller(curParam, numParam);
	}

	@GET
	@Path("/{currency}/{lastDays}/text")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public ExchangeRatesSeries getStreamTEXT(@PathParam("currency") String curParam,
			@PathParam("lastDays") String numParam) throws MalformedURLException {
		return unmarshaller(curParam, numParam);
	}

	@GET
	@Path("/{currency}/{lastDays}/average/text")
	@Produces("text/plain")
	public String calculateAverage(@PathParam("currency") String curParam, @PathParam("lastDays") String numParam,
			@PathParam("format") String formatParam) throws MalformedURLException {
		syl = getStream(curParam, numParam).getRates();

		return "Œredni kurs" + curParam.toUpperCase() + " z ostatnich " + numParam + " dni to : " + calculateAverage();
	}

	@GET
	@Path("/{currency}/{lastDays}/average/xml")
	@Produces(MediaType.APPLICATION_XML)
	public String getAverageXml(@PathParam("currency") String curParam, @PathParam("lastDays") String numParam)
			throws MalformedURLException {
		syl = getStream(curParam, numParam).getRates();

		return "<currency-webapp><currency-code>" + curParam + "</currency-code><average>" + calculateAverage()
				+ "</average><last-days>" + numParam + "</last-days></currency-webapp>";
	}

	@GET
	@Path("/{currency}/{lastDays}/average/json")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAverageJson(@PathParam("currency") String curParam, @PathParam("lastDays") String numParam)
			throws MalformedURLException {
		syl = getStream(curParam, numParam).getRates();

		return "{ \"currency\": " + curParam.toUpperCase() + ", \r\n \"last\": " + numParam + ", \r\n \"average\": "
				+ calculateAverage() + "}";
	}

	@GET
	@Path("/{currency}/{lastDays}/average/html")
	@Produces("text/html")
	public String getAverageHtml(@PathParam("currency") String curParam, @PathParam("lastDays") String numParam)
			throws MalformedURLException {
		syl = getStream(curParam, numParam).getRates();

		return "<!DOCTYPE html>\r\n" + "<html>\r\n" + "<body>\r\n" + "<h1><b> " + calculateAverage() + " </b></h1>\r\n"
				+ "<p>Kurs œredni " + curParam.toUpperCase() + " z ostatnich " + numParam + " dni </p>\r\n"
				+ "</body>\r\n" + "</html>\r\n";

	}
}
