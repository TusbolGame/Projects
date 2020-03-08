package com.ticketadvantage.services.dao.sites.metallica.captcha;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import com.aspose.ocr.ImageStream;
import com.aspose.ocr.OCRConfig;
import com.aspose.ocr.OcrEngine;

/**
 * 
 * @author jmiller
 *
 */
public class CaptchaReader {
	private static final Logger LOGGER = Logger.getLogger(CaptchaReader.class);
	private static String OS = null;

	static {
		if (OS == null) {
			OS = System.getProperty("os.name");
		}
	}

	/**
	 * 
	 */
	public CaptchaReader() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (CaptchaReader.isMac()) {
			LOGGER.error("OS: " + CaptchaReader.OS);
		}

		final CaptchaReader captchaReader = new CaptchaReader();
		// final String captchaString =
		// captchaReader.captchaProcess("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");
		final String text = captchaReader.parseCaptcha("iVBORw0KGgoAAAANSUhEUgAAASwAAABaCAIAAACNE&#x2F;xKAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAT60lEQVR4nO3deVAT5&#x2F;8H8M2dEBJCIBy5uIPlCAgInliLOvWqZ0Wt1nqOTqfWllprR6ujHVsd61jtAbY6HpU6Hu1IM7XVFtsqniMgIAwoBBQNAXJIYkhIsvn98fjb7&#x2F;7CUQ6&#x2F;v23h8&#x2F;orz+5md7PknWd3n+dZMAwAQCkahmEej4fq3QBgiKLRaHSq9wGAoQ5CCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUAxCCADFIIQAUIxJ9Q6AfwSHw1FRUWEwGK5cueLn5+dyuVpbW6dPn47jeGJiYmBgINU7OJjBv0b713O73WVlZZWVlXq9vqOjg81m+&#x2F;v7y+VylUqlVCppNFoP7&#x2F;V4POfPnz916lR+fr7T6exymaioKH9&#x2F;&#x2F;&#x2F;Dw8JiYmIyMjJkzZ&#x2F;53PscQ9ewP5AH&#x2F;Tq2tre+9955EIunuD+zn57ds2bL79+93+Xar1dqPRE2cOPGrr75qbW39f&#x2F;6wg9Wzw0r1boD+2L9&#x2F;f0hISC+TEx8ff+jQIavVSry9vLxcrVb3NYGEq1evUvjZBxMMTkefo+rq6tOnT+t0OgaDEfa&#x2F;UJHBYOh0OqIYFhZmMBimT5&#x2F;evw15PJ41a9YcPHiwr2+cMmXKiRMn&#x2F;P398&#x2F;Pz16xZY7FYuluSwWC43e7u5iqVyu+++27UqFFM5n&#x2F;uKeTk5Pj5+a1fv14oFPZ1x4YyGo0GN2aem6NHj37yySe9XHjLli2TJ09ms9n92NAXX3zROYEBAQGBgYF0Ot1qtba0tNjt9s5vvHTpUlVVlUgkevPNNzsnUKVSZWZmSqXSkJAQmUxWVVXFZrPv3r2r1WobGxsbGhocDgdaMjY21m63k3+7tVrtvn37cBzfunWrr6+vTCZbv359VFSUTCaLjIzkcrn9+JhDB4Tw+WhsbPz66697ubCfn59Kpaqvr1epVH3dUGlp6bvvvksU6XT6xIkTZ8+erVAoiIkej6elpaWmpub06dO1tbXE9PT09AcPHqxcudJsNpPXGRcXN3fu3PT0dBqNxmQyAwICWCwWWmFSUpLb7XY6nS6Xq7q6evv27Vardfz48RKJhMViEWvYv38&#x2F;juPotdVqra6uXrt2LSqmpKQkJSVJpVK5XD5jxgyZTNbXjzzoQQifj5MnT3p9s3ugVqv9&#x2F;f2lUmk&#x2F;NrRr1y6Xy0UUFy9ePH&#x2F;+fBaLFR4eLpFIuFwujUZzOp3t7e2ZmZmZmZmXL18uLCy8desWjuMTJkxAlSF5hbGxsTt37kTZGzZsmEAgYDAYXhv1eDzt7e1qtVqtVl+6dCkxMVEsFpMXOHPmTHc7XFxcXFxcjF6&#x2F;&#x2F;&#x2F;77SqVSKpXKZLJly5ZlZmb24wgMPnBN+Hz4+Pi0t7ej18uXL+fz+c3NzWKxGMdxHMfFYjFRbG5ufuWVVyIiIkaMGNHXrdTU1MTGxhJFtVq9c+fOoKCg1NRUOr2LfhcOh8NgMDQ1NV24cKGgoCAnJ2f+&#x2F;PlElYVhmFQq&#x2F;frrrxkMRnJycm&#x2F;qKBzHW1tbi4uLJ02aRGT1zJkzr776al8&#x2F;S0NDQ1BQEJypwjXh8&#x2F;H5558TCUxKSpozZw5q3RaJRO3t7RwOB9UkqOjxeDgcDrkarKurO3To0OPHj10u1y+&#x2F;&#x2F;CKXy+Pi4lJSUmJiYoKCgjIyMoi2Pq9LwRkzZvj6+vYQZrQhqVQaExOTkJBw6NAhcgIxDPv444&#x2F;ZbPa4ceP4fH5vPimdTg8KCsrKyiLXlp9&#x2F;&#x2F;jl5mXnz5hUXFxsMhra2tu5+36dMmVJeXj558uTebHTQgxAOFI7jeXl5RHHatGl8Pj8lJYV8ydSdmzdv7tmz59dff21rayMmtra2lpaW5ufno2JSUtKXX345ZswYDMNKS0vJb2exWEqlsjc7KRAIsrKyVq5cSZ44efLkoKCgxMTEXiaQvF3idU1NTVlZGVGMj49&#x2F;&#x2F;fXX33jjDQzDXC7XhQsX6urqjEajwWBobGwkbu2MGTMGXXn2abuDFYRwoP766y&#x2F;iKis4ODgtLU0qlfbm65Wbm7tu3bru+qkQ7ty5c&#x2F;LkyVGjRtHp9CdPnpBnHTlyZM6cOb3cz4qKCr1eTxRZLNbLL7&#x2F;s5+fXv0tTwsWLF8m&#x2F;IKNGjeLxeOPGjXO5XHa7PTQ01G632+12p9NZX1&#x2F;f0NBgMBgMBsOwYcNCQ0MHst3BBEL4jF6vLykpEQgEAoFAKBT+&#x2F;vvvTCbT399fr9dXV1cLhUKBQPDLL78IBAK73b579+64uDgMw5xO57Zt24iVLFiwQCAQhIeH&#x2F;+3mjh07Rtw&#x2F;JKhUKp1O59V4IBQK09LS7t27Fxsb63V2V19fn5ycHB0dHRMTEx0dHR0dnZKSolarfX19O2&#x2F;xyJEjXttSqVTDhg3ruV9bz2w2G&#x2F;kM2c&#x2F;P78UXX1QoFBwOh8Ph8Pn8gIAANMvtdmu1WrvdbrPZ7HY7juMDDP9gAiF85vr167NmzerNksHBwU6n02KxCASCysrKP&#x2F;&#x2F;8E03ncrlZWVlSqfRvW&#x2F;&#x2F;cbvfGjRuJYkZGxqJFiyQSCWrmbmlpKSsrQ61zt2&#x2F;fVqvVgYGBqN7Izs6+ffs2eVU4jtfU1NTU1BBTGAxGRkZGTk6OVyV548YNcjEuLo7NZg+wZ&#x2F;a9e&#x2F;e8zkVFIlGXVRyDwYiOjh7ItgYxCOEzhw8f7uWSWVlZLS0tSUlJGIZt3ryZmO7v75+TkyMWi6VSKZ&#x2F;Pd7lckyZN4nA4qAmBvIatW7c2NTWh1wkJCVu2bGGz2TExMVKp1OVy2Wy2zMxMHMefPHny448&#x2F;orYHlM+lS5eePn361q1bPeye2+2+evVqXV2dyWRasWIFmtjU1OSV3rS0NHLTYv&#x2F;s3buXXJw5c2ZAQAD0mOkPqnrN&#x2F;XOYTKbg4ODeHCsajbZ3797S0lKPx1NRUdGbL9yIESPQ8oSoqChi7tSpUwsLC202W5c75nA4iouLHz16RN7V+fPnd3nC2dmhQ4fQu4qKirxmHT16VKfTDeSgtbS0kA9aYGDg4cOHtVrtQNY5BGFQEyIsFmvbtm2lpaVtbW02my0sLAy1+&#x2F;H5fK1W+&#x2F;333xNLLly4UKVSobO4&#x2F;Px88j2J7ty6dSstLa2kpCQhIQHDMJ1Op9Vqibm&#x2F;&#x2F;fabQqHgcrllZWVKpTIhIYFcQbHZ7OHDh5PXJhKJvv322xs3bpSWltbW1j569Eir1T5+&#x2F;NhoNHbe9DvvvINqp9bWVvJ0LpfL4&#x2F;H6elPUy5UrV8h3elpbW5cvX75gwYKIiAiZTDZz5ky5XD6Q9Q8d0Fj&#x2F;TEdHR0lJicvlcrlcEokEvTh+&#x2F;Pi+ffuIZZYuXfrqq69KJJK0tDQ6nR4QEEB89ePj4+12u8lkMpvNXm1xSHZ29pEjR7hcbmVlZXx8fA97Eh4enpWVtXDhwqysrO6WQY3mra2tT548efr0qcPhaG9vr6mp0Wg0165dIy+Zl5e3evXqU6dOZWdnExMZDEZ+fv7s2bMH0kiwePHiEydOdDfX19dXLpfLZDKZTJaRkTFv3jzUtbXfmxusaOjOGISwS7m5uZs2bUKd0dhs9ttvvz1+&#x2F;PjIyEiVSsVgMOrq6g4fPszhcKKiohgMBnF+6HQ6tVqt0Wjcs2cPuRd1QkLC8ePHk5OTLRaLWq2ur6&#x2F;veetsNru2traXlQm6krRYLEajcfr06Q8ePCBm7dixY+PGjRcuXPAatHHgwIHVq1f3rwc5hmEmkykuLo64su2NzMxMdM+5f1scrGBQb9ccDseOHTuIw8RkMjdv3qzRaG7dukVe7OnTp+Xl5ffv329qauro6LDb7VarFbWDmUymzz77jHys4+LiCgsLcRz3eDxlZWU+Pj5&#x2F;++dZu3at2+3u685PmzaN&#x2F;Afev3+&#x2F;0WisqKjwWvnixYtRj5b++emnn&#x2F;r6bVu9enVlZSU6AoCAwTVhZ263e8mSJadOnUJFOp2+YcOGkSNHhoWFoYs6go+Pj9cU1DiGXi9atCgnJ4eYFRIS4vF40M9eYmLitWvXvvzyS5FI1NjYqNPphEKhzWa7cuUK0f0NwzCdTvfgwYPw8HC32925U3WXcBy&#x2F;fPkyeX8iIyMxDAsPDw8ODiZfwp0&#x2F;f76mpiY1NbV3R8Xb2bNnycUNGzb4+voWFBSg36AuRyqmpaV1dHQMpFlysIIQ&#x2F;h84jk+cOPGPP&#x2F;5ARX9&#x2F;&#x2F;w0bNqjV6ri4uN40wZOVl5eTi&#x2F;Hx8eTaT61W79q1q62traWlxWq1+vn5PX369Pjx4+QecEFBQRaL5fz58x988MGBAwfGjRv3t9&#x2F;gvXv3ku8VjR49mkaj8Xg8Lpc7bdo0cjOMwWDIy8vrx8hgDMPa2touXLhAFBUKRXp6ulwuz8jIQCM86uvri4qKUCAbGxv1en1MTIxEIoFeMl2CEP6Hx+NZt24dkUBfX9&#x2F;t27dHRESEhYVFRET0dW1EIz4ybNgwHx8fojLEMEwkEolEInLnz6qqKnIIFQqF2+2+dOlSWVnZ+PHjY2JiFi1aNGXKlLS0tC4rxsLCwi1btpCnZGRkMJlMNFJh1apVR44cId80+uabb4xG40cffdTX51w8fPjw8ePHRFGpVPJ4PNQHFfVWU6lU6enpqMOayWS6f&#x2F;8+l8tlsVgikahPGxpCKD4p&#x2F;mcwm80pKSnEMeHxeKdOndJoNP1r+DIYDOQjPGHCBI1G09bWNnLkyIkTJ545c8Zut7tcLvJbKioqyG0GcXFxGo3mwYMHnfMWERExZ86cbdu2HT169Keffjp79uynn36anJzstVhGRoZGoyGe8mS327u83RoZGZmbm1tSUoJGeHTmcrnKy8vJU1atWkVew+7du2&#x2F;evNndoWhubkadvGtra&#x2F;txJAc9DGpCpLq6esWKFcTY09DQ0LfeeksgEMTGxnY+C3W5XD3f4nv69Cn5apDD4WRlZfH5&#x2F;IcPH16&#x2F;fh3DsN9++00gEPj4+CgUisTERC6XW1RUdPfuXeKxLnQ6ffr06SwWq6SkpPOzXrRarVar&#x2F;eGHH3rYBz8&#x2F;v3nz5jGZTKJjGofD0Wg0WVlZV69eJS9ZV1e3Zs0aDMNSU1NVKhUa9GQ0GrVaLeo9ZzKZpk6dSpzKOhyOkpIS4u1CoTAkJKSHKk4ikfTwPDjwDNW&#x2F;BRS7e&#x2F;cuuQtlaGjosWPHfv7555aWli6XX7Zs2ZIlS4qKiq5fv+52u71u9127do1co2IYNmfOHI1G8&#x2F;jx4w8&#x2F;&#x2F;LCXf5G5c+dqNJqKioobN268&#x2F;PLLfb2ZQafTt2&#x2F;frtFoKisrvXa+paWlrxe3GIZt27aNOCOorq4mzxo5ciSq5P8Lf5khAYOasKGhYdKkSUSHErFYvGPHDrFYHBER0V3n5sbGxosXLx4&#x2F;fhzDsMDAQPSwXZVKJRKJzGZzYWEhecCRUqlcuHAhnU4PDg726rbSnezs7Ndee41Op4eHh&#x2F;P5&#x2F;DNnzhQUFFy8ePHq1av37t3rsicAGY&#x2F;HW79+fUpKCqrJveYGBgZeu3YtNzf35MmTXnHqQVtbm16vR+l99OgReRYaJwED5AdiSDfWP3z4cPTo0Y2NjajIZrMXLlwoFAqjo6NRZxShUPjkyZO7d+8mJSWhzsqlpaVe&#x2F;ch6EB4efuDAAQaDMXbsWIFAYDabCwoKCgsLr1+&#x2F;3tTURKPRUE8AHx8fGo2Gkjx16lT0oJf09HTim93R0fHw4UOdTnfv3r3Kyko0ME+v15vNZrvdzmAwhEKhQqFQq9XDhw9XKBRMJlMqlSYnJ3dXhVosFhTsqqqqmzdv&#x2F;u0HKSgoSE5ORv3pZs2ade7cOWLWwYMH09PTUXd20A9D&#x2F;fEW586dIxKIYVhHR8fRo0e7XNJisZSXl6tUqoaGBiaTSX7UUpf8&#x2F;f2nTZs2e&#x2F;ZsJpOJKiUMw0Qi0ZIlSyZPntzY2Gg0Gj0ej8Fg0Ov15Geu8Xi86OhouVxO7uHFZrOjoqKioqJSU1PRff+2tjar1dq5VmQwGGhAhlwu7+EkViAQZGdnjxs3rqmpqaioSK&#x2F;X19bWlpSU4DiO+rWyWKzQ0NDIyEiTyTRixAg6nU7sT+eaEKrBARrSISQ3dvUsNjbWaDQymcyXXnrpzp07mzZtMplMjx49MhqNXC63ubkZ5UEkEqFRtmPHjg0MDJRIJPHx8eR7njQaLSQkBDXcYxhmsVgsFotQKHQ4HDiOs1gsX1&#x2F;fHvpz8ng8uVyO+rJ5PJ729nbiLiuLxeLxeCwWi8Fg9OYakslkKpVKpVKpUCjMZrNOp0P9BJqamvh8vq+vL3klDAYDPV6tqqqK&#x2F;LC21NRUDocDrX8DNKRD6PXwPzLUhcXlcqFx4iqVisfjMRgMgUCgUqn2799vtVrNZjPqGtLR0WGxWJxOJ4&#x2F;HQ51Io6KiJBKJWCzuLg9oulAoRIOhUFXZJzQazcfHpzfd33oWGhoaGhoaGxvrdDqtVqvRaKTT6Uwmk8PhoOdwi8ViNAXDsBdeeKG5uXnnzp2lpaVGozE5OZlOp&#x2F;dj5wHZkL4mzMvLk8lk1dXVNpsNDVxSKpXt7e02my0mJgbDMKfTiZ6Hi2FYREQEeqQFGY7jNBrNZrNZrVaHw8FkMsViMZvNHtzDBaqqqkwmE3pWBZ&#x2F;PnzBhAtV79C8Goyiw5uZms9mMBi4JBALiei8gIMBrIo&#x2F;Ho3pn&#x2F;3GMRqPT6ezleGjQJQghABSj0WiD+awJgH8FCCEAFIMQAkAxCCEAFIMQAkAxCCEAFIMQAkAxCCEAFIMQAkAxCCEAFIMQAkAxCCEAFIMQAkAxCCEAFIMQAkAxCCEAFIMQAkAxCCEAFIMQAkAxCCEAFIMQAkAxCCEAFIMQAkAxCCEAAICh7X8AB61mnRAetyAAAAAASUVORK5CYII=");
		LOGGER.error("text: " + text);

//		final String captchaString = CaptchaReader.tesserectRead();
//		LOGGER.error("CaptchaString: " + captchaString);
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isWindows() {
		return OS.startsWith("Windows");
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isMac() {
		return OS.startsWith("Mac");
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isLinux() {
		return OS.startsWith("Linux");
	}

	/**
	 * 
	 * @param captchaStr
	 * @return
	 */
	public String captchaProcess(String captchaStr) {
		decodeToImage(captchaStr);
		return tesserectRead();
	}

	/**
	 * 
	 * @param imageString
	 */
	public static void decodeToImage(String imageString) {
		BufferedImage image = null;

		try {
			// BASE64Decoder decoder = new BASE64Decoder();
			// byte[] imgBytes = decoder.decodeBuffer(imageString);

			byte[] imgBytes = Base64.getDecoder().decode(imageString);
			image = ImageIO.read(new ByteArrayInputStream(imgBytes));
			padding(image);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param image
	 */
	public static void padding(BufferedImage image) {
		final BufferedImage newImage = new BufferedImage(image.getWidth() + 2 * 200, image.getHeight(),
				image.getType());
		final Graphics g = newImage.getGraphics();

		g.setColor(Color.white);
		g.fillRect(0, 0, image.getWidth() + 2 * 200, image.getHeight());
		g.drawImage(image, 200, 0, null);
		g.dispose();

		try {
			if (isLinux()) {
				ImageIO.write(newImage, "png", new File("/opt/dest.png"));
			} else {
				ImageIO.write(newImage, "png", new File("dest.png"));
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static String tesserectRead() {
		String input_file = null;
		String output_file = "output";
		if (CaptchaReader.isLinux() || CaptchaReader.isMac()) {
			input_file = "/opt/dest.png";
		} else {
			input_file = "dest.png";
		}
		if (CaptchaReader.isLinux() || CaptchaReader.isMac()) {
			output_file = "/opt/output";
		} else {
			output_file = "output";
		}

		String tesseract_install_path = null;
		final String[] command = new String[1];

		// Check which operating system this is running from
		if (CaptchaReader.isWindows()) {
			tesseract_install_path = "E:\\ProgramFiles\\Tesseract-OCR\\tesseract";
			command[0] = "cmd";
		} else if (CaptchaReader.isMac()) {
			tesseract_install_path = "/usr/local/bin/tesseract";
			command[0] = "sh";
		} else if (CaptchaReader.isLinux()) {
			tesseract_install_path = "/usr/bin/tesseract";
			command[0] = "sh";
		}

		Process p = null;
		try {
			p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();

			final PrintWriter stdin = new PrintWriter(p.getOutputStream());
			stdin.println("\"" + tesseract_install_path + "\" \"" + input_file + "\" \"" + output_file + "\" -l eng");
			stdin.close();
			p.waitFor();

			String str = Read_File.read_a_file(output_file + ".txt");
			return str.replaceAll("\\s", "");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * 
	 * @param imageString
	 * @return
	 */
	public String parseCaptcha(String imageString) {
		LOGGER.info("Entering parseCaptcha()");
		// create a buffered image
		BufferedImage image = null;
		byte[] imageByte;
		String result = "";

		try {
			imageByte = DatatypeConverter.parseBase64Binary(imageString);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();

			// write the image to a file
			String input_file = null;
			if (CaptchaReader.isLinux() || CaptchaReader.isMac()) {
				input_file = "/opt/dest.png";
			} else {
				input_file = "dest.png";
			}

			final File outputfile = new File(input_file);
			ImageIO.write(image, "png", outputfile);
			result = getTextFromImageFile(input_file);

			result = result.trim();
			LOGGER.debug("Image code is=" + result + "...");
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting parseCaptcha()");
		return result;
	}

	/**
	 * 
	 * @param imageFile
	 * @return
	 */
	public String getTextFromImageFile(String imageFile) {
		System.out.println("image file=" + imageFile);
		OcrEngine ocrEngine = new OcrEngine();
		OCRConfig ocrConfig = ocrEngine.getConfig();
		ocrConfig.setWhitelist(new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E',
				'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
				'v', 'w', 'x', 'y', 'z' });

		// Set the Image property by loading the image from file path location
		ocrEngine.setImage(ImageStream.fromFile(imageFile));

		// Process the image
		if (ocrEngine.process()) {
			String result = "" + ocrEngine.getText();
			return result;
		} else {
			return "";
		}
	}
}