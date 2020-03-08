package com.ticketadvantage.services.dao.reports;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import com.ticketadvantage.services.dao.sites.kenpom.KenPomData;
import com.ticketadvantage.services.dao.sites.teamrankings.TeamRankingsData;
import com.ticketadvantage.services.dao.sites.usatoday.SagarinData;
import com.ticketadvantage.services.model.SpreadLastThree;

/**
 * 
 * @author jmiller
 *
 */
public class BillCPANcaaBasketballReport extends NcaaBasketballReport {
	private final static Logger LOGGER = Logger.getLogger(BillCPANcaaBasketballReport.class);

	/**
	 * 
	 */
	public BillCPANcaaBasketballReport() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	public StreamingOutput createNcaaReport() {
		StreamingOutput stream = null;

		try {
			final HSSFWorkbook workbook = generateReportWorkbook();

			// Write the output to a file
			stream = new StreamingOutput() {
				public void write(OutputStream output) throws IOException, WebApplicationException {
					try {
						workbook.write(output);
					} catch (Exception e) {
						throw new WebApplicationException(e);
					}
				}
			};
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return stream;
	}

	/**
	 * 
	 * @return
	 */
	public HSSFWorkbook generateReportWorkbook() {
		LOGGER.debug("Entering generateReportWorkbook()");

		// Setup the workbook
		final HSSFWorkbook workbook = new HSSFWorkbook();

		// Report Data
		setupReportData();

		final HSSFSheet cal = workbook.createSheet("Cal");
		setupCalHeader(cal);

		// Setup the data sheets first
		setupNcaabExcel(workbook);

		final HSSFCellStyle percentStyle = workbook.createCellStyle();
		percentStyle.setDataFormat(workbook.createDataFormat().getFormat("##.##%"));

		int count = 0;
		for (KenPomData kpd : lkpd) {
			LOGGER.debug("KenPomData: " + kpd);
			if (kpd.getRoadTeam() != null && kpd.getRoadTeam().length() > 0 && 
				kpd.getHomeTeam() != null && kpd.getHomeTeam().length() > 0) {
				// Create the worksheet
				String roadTeam = kpd.getRoadTeam();
				roadTeam = roadTeam.replace("'", "");
				String homeTeam = kpd.getHomeTeam();
				homeTeam = homeTeam.replace("'", "");
				String sheetName = roadTeam + " vs " + homeTeam;
				if (sheetName.length() > 30) {
					sheetName = sheetName.substring(0, 30);
				}

				HSSFSheet sheet = null;
				try {
					sheet = workbook.createSheet(sheetName);
				} catch (IllegalArgumentException iae) {
					LOGGER.error(iae.getMessage(), iae);
					if (iae.getMessage().contains("The workbook already contains a sheet named")) {
						sheetName = roadTeam + "2 vs " + homeTeam;
						if (sheetName.length() > 30) {
							sheetName = sheetName.substring(0, 30);
						}
						try {
							sheet = workbook.createSheet(sheetName);
						} catch (IllegalArgumentException iaedos) {
							LOGGER.error(iaedos.getMessage(), iaedos);
							if (iaedos.getMessage().contains("The workbook already contains a sheet named")) {
								sheetName = roadTeam + "3 vs " + homeTeam;
								if (sheetName.length() > 30) {
									sheetName = sheetName.substring(0, 30);
								}
								sheet = workbook.createSheet(sheetName);
							}
						} catch (Exception e) {
							LOGGER.error(e.getMessage(), e);
						}
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
				int siteType = -4;
				for (KenPomData kp : lkpd) {
					if (kp.getHomeTeam() != null && kp.getHomeTeam().equals(homeTeam) &&
						kp.getRoadTeam() != null && kp.getRoadTeam().equals(roadTeam)) {
						siteType = kp.getSiteLocation();
					}
				}

				createNcaab2018ModelForTeam(percentStyle, sheet, kpd, roadTeam, homeTeam, siteType);
				createNcaab2017ModelForTeam(percentStyle, sheet, kpd, roadTeam, homeTeam, siteType);
				createNcaabLast3ModelForTeam(percentStyle, sheet, kpd, roadTeam, homeTeam, siteType);
				createNcaabGiantKillersModelForTeam(percentStyle, sheet, kpd, roadTeam, homeTeam, siteType);
				setupGameInfo(cal, count++, roadTeam, homeTeam, siteType, sheetName);
			}
		}

		return workbook;
	}

	/**
	 * 
	 * @param cal
	 * @param count
	 * @param roadTeam
	 * @param homeTeam
	 * @param siteType
	 * @param sheetName
	 */
	public void setupGameInfo(HSSFSheet cal, int count, String roadTeam, String homeTeam, int siteType, String sheetName) {
		HSSFRow rowhead = cal.createRow(count+2);
		count = count + 3;
		rowhead.createCell(0).setCellValue("");
		
		String roadNumber = "";
		String homeNumber = "";

		if (masseyComposite != null && masseyComposite.size() > 0) {
			final Iterator<String> itr = masseyComposite.keySet().iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				String value = masseyComposite.get(key);
	
				if (key != null && roadTeam != null && homeTeam != null) {
					key = key.replace("'", "");
					if (key.equals(roadTeam)) {
						roadNumber = value;
					} else if (key.equals(homeTeam)) {
						homeNumber = value;
					}
				}
			}
		}
		rowhead.createCell(1).setCellValue("(" + roadNumber + ") " + roadTeam);
		rowhead.createCell(2).setCellValue("(" + homeNumber + ") " + homeTeam);

		HSSFCell columnd = rowhead.createCell(3);
		HSSFCell columne = rowhead.createCell(4);
		HSSFCell columnf = rowhead.createCell(5);
		HSSFCell columng = rowhead.createCell(6);
		HSSFCell columnh = rowhead.createCell(7);
		HSSFCell columni = rowhead.createCell(8);
		HSSFCell columnj = rowhead.createCell(9);
		HSSFCell columnk = rowhead.createCell(10);
		HSSFCell columnl = rowhead.createCell(11);
		HSSFCell columnm = rowhead.createCell(12);
		HSSFCell columnn = rowhead.createCell(13);

		columnd.setCellType(CellType.FORMULA);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.FORMULA);
		columnh.setCellType(CellType.NUMERIC);
		columni.setCellType(CellType.NUMERIC);
		columnj.setCellType(CellType.NUMERIC);
		columnk.setCellType(CellType.FORMULA);
		columnl.setCellType(CellType.FORMULA);
		columnm.setCellType(CellType.FORMULA);
		columnn.setCellType(CellType.FORMULA);

		columnd.setCellFormula("'" + sheetName + "'" + "!D59");
		columne.setCellFormula("'" + sheetName + "'" + "!D29");
		columnf.setCellFormula("'" + sheetName + "'" + "!D89");
		columng.setCellFormula("'" + sheetName + "'" + "!D119");

		float teamOne = 0;
		float teamTwo = 0;
		for (SagarinData sd : sagarinRatings) {
			String sdTeam = sd.getTeam();
			if (sdTeam != null) {
				sdTeam = sdTeam.replace("'", "");
	
				if (sdTeam.equals(roadTeam)) {
					teamOne = sd.getValue();
				} else if (sdTeam.equals(homeTeam)) {
					teamTwo = sd.getValue();
				}
			}
		}
		
		// Setup the Sagarin Ratings
		columnh.setCellValue(teamOne - teamTwo - siteType);

		int kpdSpread = 0;
		for (KenPomData kpd : lkpd) {
			String kpdRoadTeam = kpd.getRoadTeam();
			String kpdHomeTeam = kpd.getHomeTeam();
			if (kpdRoadTeam != null && kpdHomeTeam != null) {
				kpdRoadTeam = kpdRoadTeam.replace("'", "");
				kpdHomeTeam = kpdHomeTeam.replace("'", "");
				
				if (kpdRoadTeam.equals(roadTeam) &&
					kpdHomeTeam.equals(homeTeam)) {
					kpdSpread = kpd.getSpread();
				}
			}
		}

		columni.setCellValue(kpdSpread);
		// columnj.setCellValue(); Massey
		columnk.setCellFormula("AVERAGE(H" + count + ":I" + count + ")");
		columnl.setCellFormula("D" + count + "*0.2+E" + count + "*0.2+F" + count + "*0.05+G" + count + "*0.1+H" + count + "*0.25+I" + count + "*0.2");
		columnm.setCellFormula("(ABS(L" + count + "-K" + count + "))");
	}

	/**
	 * 
	 * @param cal
	 */
	public void setupCalHeader(HSSFSheet cal) {
		HSSFRow rowhead = cal.createRow(0);
		rowhead.createCell(0).setCellValue("");
		rowhead.createCell(1).setCellValue("Spread in Terms of Home Team");

		rowhead = cal.createRow(1);
		rowhead.createCell(0).setCellValue("");
		rowhead.createCell(1).setCellValue("Road Team");
		rowhead.createCell(2).setCellValue("Home Team");
		rowhead.createCell(3).setCellValue("2017");
		rowhead.createCell(4).setCellValue("2018");
		rowhead.createCell(5).setCellValue("Last 3");
		rowhead.createCell(6).setCellValue("GK");
		rowhead.createCell(7).setCellValue("Sagarian");
		rowhead.createCell(8).setCellValue("Ken Pom");
		rowhead.createCell(9).setCellValue("Massey");
		rowhead.createCell(10).setCellValue("Spread");
		rowhead.createCell(11).setCellValue("BP computed");
		rowhead.createCell(12).setCellValue("Difference");
		rowhead.createCell(13).setCellValue("Massey Composite");
	}

	/**
	 * 
	 * @param percentStyle
	 * @param sheet
	 * @param n
	 * @param teamOne
	 * @param teamTwo
	 * @param siteType
	 */
	public void createNcaab2018ModelForTeam(HSSFCellStyle percentStyle, HSSFSheet sheet, KenPomData kpd, String teamOne, String teamTwo, int siteType) {
		int count = 0;
		HSSFRow rowhead = sheet.createRow(count);
		rowhead.createCell(0).setCellValue("2018 Model");
		rowhead.createCell(1).setCellValue("");
		rowhead.createCell(2).setCellValue(teamOne);
		rowhead.createCell(3).setCellValue(teamTwo);

		// Row2 - Effective FG %
		rowhead = sheet.createRow(count + 1);
		rowhead.createCell(0).setCellValue("Effective FG %");
		rowhead.createCell(1).setCellValue("");
		HSSFCell columnc = rowhead.createCell(2);
		HSSFCell columnd = rowhead.createCell(3);
		float efpint1 = 0;
		float efpint2 = 0;
		for (TeamRankingsData efp : effectiveFGPct) {
			if (efp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = efp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (efp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = efp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		HSSFCell columne = rowhead.createCell(4);
		HSSFCell columnf = rowhead.createCell(5);
		HSSFCell columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F2*G2");
		columnf.setCellFormula("C2-D2");
		columng.setCellValue(60);

		// Row3 - Opponent Effective FG %
		rowhead = sheet.createRow(count + 2);
		rowhead.createCell(0).setCellValue("Opponent Effective FG %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oefp : opponentEffectiveFGPct) {
			if (oefp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = oefp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (oefp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = oefp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F3*G3");
		columnf.setCellFormula("-(C3-D3)");
		columng.setCellValue(60);

		// Row4 - 2 Point %
		rowhead = sheet.createRow(count + 3);
		rowhead.createCell(0).setCellValue("2 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpp : twoPointPct) {
			if (tpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = tpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (tpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = tpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row5 - Opponent 2 Point %
		rowhead = sheet.createRow(count + 4);
		rowhead.createCell(0).setCellValue("Opponent 2 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpp : opponentTwoPointPct) {
			if (otpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = otpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (otpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = otpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row6 - 3 Point %
		rowhead = sheet.createRow(count + 5);
		rowhead.createCell(0).setCellValue("3 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpp : threePointPct) {
			if (tpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = tpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (tpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = tpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row7 - Opponent 3 Point %
		rowhead = sheet.createRow(count + 6);
		rowhead.createCell(0).setCellValue("Opponent 3 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpp : opponentThreePointPct) {
			if (otpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = otpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (otpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = otpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row8 - Free Throw %
		rowhead = sheet.createRow(count + 7);
		rowhead.createCell(0).setCellValue("Free Throw %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ftp : freeThrowPct) {
			if (ftp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = ftp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (ftp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = ftp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F8*G8");
		columnf.setCellFormula("C8-D8");
		columng.setCellValue(30);

		// Row9 - Possessions Per Game
		rowhead = sheet.createRow(count + 8);
		rowhead.createCell(0).setCellValue("Possessions Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ppg : possessionsPerGame) {
			if (ppg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.NUMERIC);
				String efp1 = ppg.getField3();
				efpint1 = Float.parseFloat(efp1);
				columnc.setCellValue(efpint1);
			} else if (ppg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ppg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row10 - Offensive Efficiency
		rowhead = sheet.createRow(count + 9);
		rowhead.createCell(0).setCellValue("Offensive Efficiency");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oe : offensiveEfficiency) {
			if (oe.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.NUMERIC);
				String efp1 = oe.getField3();
				efpint1 = Float.parseFloat(efp1);
				columnc.setCellValue(efpint1);
			} else if (oe.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oe.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row11
		rowhead = sheet.createRow(count + 10);
		rowhead.createCell(0).setCellValue("");

		// Row12 - Steals Per Game
		rowhead = sheet.createRow(count + 11);
		rowhead.createCell(0).setCellValue("Steals Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData spg : stealsPerGame) {
			if (spg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = spg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (spg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = spg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F12+F13) * 0.5");
		columnf.setCellFormula("C12-D12");

		// Row13 - Opponents Steals Per Game
		rowhead = sheet.createRow(count + 12);
		rowhead.createCell(0).setCellValue("Opponents Steals Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ospg : opponentStealsPerGame) {
			if (ospg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ospg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (ospg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp1 = ospg.getField3();
				efpint1 = Float.parseFloat(efp1);
				columnd.setCellValue(efpint1);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C13-D13)");

		// Row14 - Blocks Per Game
		rowhead = sheet.createRow(count + 13);
		rowhead.createCell(0).setCellValue("Blocks Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData bpg : blocksPerGame) {
			if (bpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = bpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
				columnc.setCellValue(efpint1);
			} else if (bpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = bpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F14+F15) * 0.4");
		columnf.setCellFormula("C14-D14");

		// Row15 - Opponents Blocks Per Game
		rowhead = sheet.createRow(count + 14);
		rowhead.createCell(0).setCellValue("Opponents Blocks Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData obpg : opponentBlocksPerGame) {
			if (obpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = obpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (obpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = obpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C15-D15)");

		// Row16 - Offensive Rebounds Per Game
		rowhead = sheet.createRow(count + 15);
		rowhead.createCell(0).setCellValue("Offensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData orpg : offensiveReboundsPerGame) {
			if (orpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = orpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (orpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = orpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F16+F17) * 0.5");
		columnf.setCellFormula("C16-D16");

		// Row17 - Opponent Offensive Rebounds Per Game
		rowhead = sheet.createRow(count + 16);
		rowhead.createCell(0).setCellValue("Opponent Offensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oorpg : oponentOffensiveReboundsPerGame) {
			if (oorpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = oorpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (oorpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oorpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C17-D17)");

		// Row18 - Defensive Rebounds Per Game
		rowhead = sheet.createRow(count + 17);
		rowhead.createCell(0).setCellValue("Defensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData drpg : defensiveReboundsPerGame) {
			if (drpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = drpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (drpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = drpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F18+F19) * 0.25");
		columnf.setCellFormula("C18-D18");

		// Row19 - Opponent Defensive Rebounds Per Game
		rowhead = sheet.createRow(count + 18);
		rowhead.createCell(0).setCellValue("Opponent Defensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData odrpg : opponentDefensiveReboundsPerGame) {
			if (odrpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = odrpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (odrpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = odrpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C19-D19)");

		// Row20 - Assists Per Game
		rowhead = sheet.createRow(count + 19);
		rowhead.createCell(0).setCellValue("Assists Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData apg : assistsPerGame) {
			if (apg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = apg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (apg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = apg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F20+F21) * 0.5");
		columnf.setCellFormula("C20-D20");

		// Row21 - Opponents Assists Per Game
		rowhead = sheet.createRow(count + 20);
		rowhead.createCell(0).setCellValue("Opponents Assists Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oapg : opponentAssistsPerGame) {
			if (oapg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = oapg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (oapg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oapg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C21-D21)");

		// Row22 - Turnovers Per Game
		rowhead = sheet.createRow(count + 21);
		rowhead.createCell(0).setCellValue("Turnovers Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpg : turnoversPerGame) {
			if (tpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = tpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (tpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = tpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F22+F23) * 0.5");
		columnf.setCellFormula("-(C22-D22)");

		// Row23 - Opponents Turnovers Per Game
		rowhead = sheet.createRow(count + 22);
		rowhead.createCell(0).setCellValue("Opponents Turnovers Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpg : opponentTurnoversPerGame) {
			if (otpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = otpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (otpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = otpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("(C23-D23) * 0.6");

		// Row24 - Personal Fouls Per Game
		rowhead = sheet.createRow(count + 23);
		rowhead.createCell(0).setCellValue("Personal Fouls Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData pfpg : personalFoulsPerGame) {
			if (pfpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = pfpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (pfpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = pfpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F24+F25) * 0.4");
		columnf.setCellFormula("-(C24-D24)");

		// Row25 - Opponent Personal Fouls Per Game
		rowhead = sheet.createRow(count + 24);
		rowhead.createCell(0).setCellValue("Opponent Personal Fouls Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData opfpg : oppoenentPersonalFoulsPerGame) {
			if (opfpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = opfpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (opfpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = opfpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("C25-D25");

		// Row26 - Extra Chances Per Game
		rowhead = sheet.createRow(count + 25);
		rowhead.createCell(0).setCellValue("Extra Chances Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ecpg : extraChancesPerGame) {
			if (ecpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ecpg.getField3();
				columnc.setCellFormula(efp1 + " * F29");
			} else if (ecpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ecpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("F26 * 0.05");
		columnf.setCellFormula("C26-D26");

		// Row27 - Schedule Strength By Other
		rowhead = sheet.createRow(count + 26);
		rowhead.createCell(0).setCellValue("Schedule Strength By Other");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ssbo : scheduleStrengthByOther) {
			if (kpd.getRoadTeam().equals(ssbo.getField2())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ssbo.getField3();
				columnc.setCellFormula(efp1);
			} else if (kpd.getHomeTeam().equals(ssbo.getField2())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ssbo.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row28
		rowhead = sheet.createRow(count + 27);
		HSSFCell col0 = rowhead.createCell(0);
		col0.setCellType(CellType.STRING);
		col0.setCellValue("");

		// Row29
		rowhead = sheet.createRow(count + 28);
		col0 = rowhead.createCell(0);
		col0.setCellType(CellType.STRING);
		col0.setCellValue("Home Team Spread");
		HSSFCell col1 = rowhead.createCell(1);
		col1.setCellType(CellType.STRING);
		col1.setCellValue("");
		columnc = rowhead.createCell(2);
		columnc.setCellType(CellType.STRING);
		columnc.setCellValue("");
		columnd = rowhead.createCell(3);
		columnd.setCellType(CellType.FORMULA);
		columnd.setCellFormula("SUM(E2:E27) - D27 + C27 - " + siteType);
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columnf.setCellType(CellType.FORMULA);
		columnf.setCellFormula("D9/C9");
	}

	/**
	 * 
	 * @param percentStyle
	 * @param sheet
	 * @param n
	 * @param teamOne
	 * @param teamTwo
	 * @param siteType
	 */
	public void createNcaab2017ModelForTeam(HSSFCellStyle percentStyle, HSSFSheet sheet, KenPomData kpd, String teamOne, String teamTwo, int siteType) {
		int count = 30;
		HSSFRow rowhead = sheet.createRow(count);
		rowhead.createCell(0).setCellValue("2017 Model");
		rowhead.createCell(1).setCellValue("");
		rowhead.createCell(2).setCellValue(teamOne);
		rowhead.createCell(3).setCellValue(teamTwo);

		// Row32 - Effective FG %
		rowhead = sheet.createRow(count + 1);
		rowhead.createCell(0).setCellValue("Effective FG %");
		rowhead.createCell(1).setCellValue("");
		HSSFCell columnc = rowhead.createCell(2);
		HSSFCell columnd = rowhead.createCell(3);
		float efpint1 = 0;
		float efpint2 = 0;
		for (TeamRankingsData efp : effectiveFGPct) {
			if (efp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = efp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (efp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = efp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		HSSFCell columne = rowhead.createCell(4);
		HSSFCell columnf = rowhead.createCell(5);
		HSSFCell columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F32*G32");
		columnf.setCellFormula("C32-D32");
		columng.setCellValue(60);

		// Row33 - Opponent Effective FG %
		rowhead = sheet.createRow(count + 2);
		rowhead.createCell(0).setCellValue("Opponent Effective FG %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oefp : opponentEffectiveFGPct) {
			if (oefp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = oefp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (oefp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = oefp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F33*G33");
		columnf.setCellFormula("-(C33-D33)");
		columng.setCellValue(60);

		// Row34 - 2 Point %
		rowhead = sheet.createRow(count + 3);
		rowhead.createCell(0).setCellValue("2 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpp : twoPointPct) {
			if (tpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = tpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (tpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = tpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row35 - Opponent 2 Point %
		rowhead = sheet.createRow(count + 4);
		rowhead.createCell(0).setCellValue("Opponent 2 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpp : opponentTwoPointPct) {
			if (otpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = otpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (otpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = otpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row36 - 3 Point %
		rowhead = sheet.createRow(count + 5);
		rowhead.createCell(0).setCellValue("3 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpp : threePointPct) {
			if (tpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = tpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (tpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = tpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row37 - Opponent 3 Point %
		rowhead = sheet.createRow(count + 6);
		rowhead.createCell(0).setCellValue("Opponent 3 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpp : opponentThreePointPct) {
			if (otpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = otpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (otpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = otpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row38 - Free Throw %
		rowhead = sheet.createRow(count + 7);
		rowhead.createCell(0).setCellValue("Free Throw %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ftp : freeThrowPct) {
			if (ftp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = ftp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (ftp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = ftp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F38*G38");
		columnf.setCellFormula("C38-D38");
		columng.setCellValue(12);

		// Row39 - Possessions Per Game
		rowhead = sheet.createRow(count + 8);
		rowhead.createCell(0).setCellValue("Possessions Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ppg : possessionsPerGame) {
			if (ppg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.NUMERIC);
				String efp1 = ppg.getField3();
				efpint1 = Float.parseFloat(efp1);
				columnc.setCellValue(efpint1);
			} else if (ppg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ppg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row40 - Offensive Efficiency
		rowhead = sheet.createRow(count + 9);
		rowhead.createCell(0).setCellValue("Offensive Efficiency");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oe : offensiveEfficiency) {
			if (oe.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.NUMERIC);
				String efp1 = oe.getField3();
				efpint1 = Float.parseFloat(efp1);
				columnc.setCellValue(efpint1);
			} else if (oe.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oe.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row41
		rowhead = sheet.createRow(count + 10);
		rowhead.createCell(0).setCellValue("");

		// Row42 - Steals Per Game
		rowhead = sheet.createRow(count + 11);
		rowhead.createCell(0).setCellValue("Steals Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData spg : stealsPerGame) {
			if (spg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = spg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (spg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = spg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F42+F43) * 0.5");
		columnf.setCellFormula("C42-D42");

		// Row43 - Opponents Steals Per Game
		rowhead = sheet.createRow(count + 12);
		rowhead.createCell(0).setCellValue("Opponents Steals Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ospg : opponentStealsPerGame) {
			if (ospg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ospg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (ospg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp1 = ospg.getField3();
				efpint1 = Float.parseFloat(efp1);
				columnd.setCellValue(efpint1);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C43-D43)");

		// Row44 - Blocks Per Game
		rowhead = sheet.createRow(count + 13);
		rowhead.createCell(0).setCellValue("Blocks Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData bpg : blocksPerGame) {
			if (bpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = bpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (bpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = bpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F44+F45) * 0.4");
		columnf.setCellFormula("C44-D44");

		// Row45 - Opponents Blocks Per Game
		rowhead = sheet.createRow(count + 14);
		rowhead.createCell(0).setCellValue("Opponents Blocks Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData obpg : opponentBlocksPerGame) {
			if (obpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = obpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (obpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = obpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C45-D45)");

		// Row46 - Offensive Rebounds Per Game
		rowhead = sheet.createRow(count + 15);
		rowhead.createCell(0).setCellValue("Offensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData orpg : offensiveReboundsPerGame) {
			if (orpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = orpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (orpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = orpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F46+F47) * 0.5");
		columnf.setCellFormula("C46-D46");

		// Row47 - Opponent Offensive Rebounds Per Game
		rowhead = sheet.createRow(count + 16);
		rowhead.createCell(0).setCellValue("Opponent Offensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oorpg : oponentOffensiveReboundsPerGame) {
			if (oorpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = oorpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (oorpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oorpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C47-D47)");

		// Row48 - Defensive Rebounds Per Game
		rowhead = sheet.createRow(count + 17);
		rowhead.createCell(0).setCellValue("Defensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData drpg : defensiveReboundsPerGame) {
			if (drpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = drpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (drpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = drpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F48+F49) * 0.25");
		columnf.setCellFormula("C48-D48");

		// Row49 - Opponent Defensive Rebounds Per Game
		rowhead = sheet.createRow(count + 18);
		rowhead.createCell(0).setCellValue("Opponent Defensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData odrpg : opponentDefensiveReboundsPerGame) {
			if (odrpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = odrpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (odrpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = odrpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C49-D49)");

		// Row50 - Assists Per Game
		rowhead = sheet.createRow(count + 19);
		rowhead.createCell(0).setCellValue("Assists Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData apg : assistsPerGame) {
			if (apg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = apg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (apg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = apg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F50+F51) * 0.5");
		columnf.setCellFormula("C50-D50");

		// Row51 - Opponents Assists Per Game
		rowhead = sheet.createRow(count + 20);
		rowhead.createCell(0).setCellValue("Opponents Assists Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oapg : opponentAssistsPerGame) {
			if (oapg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = oapg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (oapg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oapg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C51-D51)");

		// Row52 - Turnovers Per Game
		rowhead = sheet.createRow(count + 21);
		rowhead.createCell(0).setCellValue("Turnovers Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpg : turnoversPerGame) {
			if (tpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = tpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (tpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = tpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F52+F53) * 0.5");
		columnf.setCellFormula("-(C52-D52)");

		// Row53 - Opponents Turnovers Per Game
		rowhead = sheet.createRow(count + 22);
		rowhead.createCell(0).setCellValue("Opponents Turnovers Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpg : opponentTurnoversPerGame) {
			if (otpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = otpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (otpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = otpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("(C53-D53)");

		// Row54 - Personal Fouls Per Game
		rowhead = sheet.createRow(count + 23);
		rowhead.createCell(0).setCellValue("Personal Fouls Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData pfpg : personalFoulsPerGame) {
			if (pfpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = pfpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (pfpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = pfpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F54+F55) * 0.4");
		columnf.setCellFormula("-(C54-D54)");

		// Row55 - Opponent Personal Fouls Per Game
		rowhead = sheet.createRow(count + 24);
		rowhead.createCell(0).setCellValue("Opponent Personal Fouls Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData opfpg : oppoenentPersonalFoulsPerGame) {
			if (opfpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = opfpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (opfpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = opfpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("C55-D55");

		// Row56 - Extra Chances Per Game
		rowhead = sheet.createRow(count + 25);
		rowhead.createCell(0).setCellValue("Extra Chances Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ecpg : extraChancesPerGame) {
			if (ecpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ecpg.getField3();
				columnc.setCellFormula(efp1 + " * F59");
			} else if (ecpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ecpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("F56 * 0.05");
		columnf.setCellFormula("C56-D56");

		// Row57 - Schedule Strength By Other
		rowhead = sheet.createRow(count + 26);
		rowhead.createCell(0).setCellValue("Schedule Strength By Other");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ssbo : scheduleStrengthByOther) {
			if (kpd.getRoadTeam().equals(ssbo.getField2())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ssbo.getField3();
				columnc.setCellFormula(efp1);
			} else if (kpd.getHomeTeam().equals(ssbo.getField2())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ssbo.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row58
		rowhead = sheet.createRow(count + 27);
		HSSFCell col0 = rowhead.createCell(0);
		col0.setCellType(CellType.STRING);
		col0.setCellValue("");

		// Row59
		rowhead = sheet.createRow(count + 28);
		col0 = rowhead.createCell(0);
		col0.setCellType(CellType.STRING);
		col0.setCellValue("Home Team Spread");
		HSSFCell col1 = rowhead.createCell(1);
		col1.setCellType(CellType.STRING);
		col1.setCellValue("");
		columnc = rowhead.createCell(2);
		columnc.setCellType(CellType.STRING);
		columnc.setCellValue("");
		columnd = rowhead.createCell(3);
		columnd.setCellType(CellType.FORMULA);
		columnd.setCellFormula("SUM(E32:E57) - D57 + C57 - " + siteType);
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columnf.setCellType(CellType.FORMULA);
		columnf.setCellFormula("D39/C39");
	}

	/**
	 * 
	 * @param percentStyle
	 * @param sheet
	 * @param n
	 * @param teamOne
	 * @param teamTwo
	 * @param siteType
	 */
	public void createNcaabLast3ModelForTeam(HSSFCellStyle percentStyle, HSSFSheet sheet, KenPomData kpd, String teamOne, String teamTwo, int siteType) {
		int count = 60;
		HSSFRow rowhead = sheet.createRow(count);
		rowhead.createCell(0).setCellValue("Last 3");
		rowhead.createCell(1).setCellValue("");
		rowhead.createCell(2).setCellValue(teamOne);
		rowhead.createCell(3).setCellValue(teamTwo);

		// Row62 - Effective FG %
		rowhead = sheet.createRow(count + 1);
		rowhead.createCell(0).setCellValue("Effective FG %");
		rowhead.createCell(1).setCellValue("");
		HSSFCell columnc = rowhead.createCell(2);
		HSSFCell columnd = rowhead.createCell(3);
		float efpint1 = 0;
		float efpint2 = 0;
		for (TeamRankingsData efp : effectiveFGPct) {
			if (efp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = efp.getField4();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (efp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = efp.getField4();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		HSSFCell columne = rowhead.createCell(4);
		HSSFCell columnf = rowhead.createCell(5);
		HSSFCell columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F62*G62");
		columnf.setCellFormula("C62-D62");
		columng.setCellValue(60);

		// Row63 - Opponent Effective FG %
		rowhead = sheet.createRow(count + 2);
		rowhead.createCell(0).setCellValue("Opponent Effective FG %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oefp : opponentEffectiveFGPct) {
			if (oefp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = oefp.getField4();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (oefp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = oefp.getField4();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F63*G63");
		columnf.setCellFormula("-(C63-D63)");
		columng.setCellValue(60);

		// Row64 - 2 Point %
		rowhead = sheet.createRow(count + 3);
		rowhead.createCell(0).setCellValue("2 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpp : twoPointPct) {
			if (tpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = tpp.getField4();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (tpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = tpp.getField4();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row65 - Opponent 2 Point %
		rowhead = sheet.createRow(count + 4);
		rowhead.createCell(0).setCellValue("Opponent 2 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpp : opponentTwoPointPct) {
			if (otpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = otpp.getField4();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (otpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = otpp.getField4();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row66 - 3 Point %
		rowhead = sheet.createRow(count + 5);
		rowhead.createCell(0).setCellValue("3 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpp : threePointPct) {
			if (tpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = tpp.getField4();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (tpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = tpp.getField4();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row67 - Opponent 3 Point %
		rowhead = sheet.createRow(count + 6);
		rowhead.createCell(0).setCellValue("Opponent 3 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpp : opponentThreePointPct) {
			if (otpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = otpp.getField4();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (otpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = otpp.getField4();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row68 - Free Throw %
		rowhead = sheet.createRow(count + 7);
		rowhead.createCell(0).setCellValue("Free Throw %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ftp : freeThrowPct) {
			if (ftp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = ftp.getField4();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (ftp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = ftp.getField4();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F68*G68");
		columnf.setCellFormula("C68-D68");
		columng.setCellValue(12);

		// Row69 - Possessions Per Game
		rowhead = sheet.createRow(count + 8);
		rowhead.createCell(0).setCellValue("Possessions Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ppg : possessionsPerGame) {
			if (ppg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.NUMERIC);
				String efp1 = ppg.getField4();
				efpint1 = Float.parseFloat(efp1);
				columnc.setCellValue(efpint1);
			} else if (ppg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ppg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row70 - Offensive Efficiency
		rowhead = sheet.createRow(count + 9);
		rowhead.createCell(0).setCellValue("Offensive Efficiency");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oe : offensiveEfficiency) {
			if (oe.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.NUMERIC);
				String efp1 = oe.getField4();
				efpint1 = Float.parseFloat(efp1);
				columnc.setCellValue(efpint1);
			} else if (oe.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oe.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row71
		rowhead = sheet.createRow(count + 10);
		rowhead.createCell(0).setCellValue("");

		// Row72 - Steals Per Game
		rowhead = sheet.createRow(count + 11);
		rowhead.createCell(0).setCellValue("Steals Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData spg : stealsPerGame) {
			if (spg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = spg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (spg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = spg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F72+F73) * 0.5");
		columnf.setCellFormula("C72-D72");

		// Row73 - Opponents Steals Per Game
		rowhead = sheet.createRow(count + 12);
		rowhead.createCell(0).setCellValue("Opponents Steals Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ospg : opponentStealsPerGame) {
			if (ospg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ospg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (ospg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp1 = ospg.getField4();
				efpint1 = Float.parseFloat(efp1);
				columnd.setCellValue(efpint1);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C73-D73)");

		// Row74 - Blocks Per Game
		rowhead = sheet.createRow(count + 13);
		rowhead.createCell(0).setCellValue("Blocks Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData bpg : blocksPerGame) {
			if (bpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = bpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (bpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = bpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F74+F75) * 0.4");
		columnf.setCellFormula("C74-D74");

		// Row75 - Opponents Blocks Per Game
		rowhead = sheet.createRow(count + 14);
		rowhead.createCell(0).setCellValue("Opponents Blocks Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData obpg : opponentBlocksPerGame) {
			if (obpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = obpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (obpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = obpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C75-D75)");

		// Row76 - Offensive Rebounds Per Game
		rowhead = sheet.createRow(count + 15);
		rowhead.createCell(0).setCellValue("Offensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData orpg : offensiveReboundsPerGame) {
			if (orpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = orpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (orpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = orpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F76+F77) * 0.5");
		columnf.setCellFormula("C76-D76");

		// Row77 - Opponent Offensive Rebounds Per Game
		rowhead = sheet.createRow(count + 16);
		rowhead.createCell(0).setCellValue("Opponent Offensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oorpg : oponentOffensiveReboundsPerGame) {
			if (oorpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = oorpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (oorpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oorpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C77-D77)");

		// Row78 - Defensive Rebounds Per Game
		rowhead = sheet.createRow(count + 17);
		rowhead.createCell(0).setCellValue("Defensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData drpg : defensiveReboundsPerGame) {
			if (drpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = drpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (drpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = drpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F78+F79) * 0.25");
		columnf.setCellFormula("C78-D78");

		// Row79 - Opponent Defensive Rebounds Per Game
		rowhead = sheet.createRow(count + 18);
		rowhead.createCell(0).setCellValue("Opponent Defensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData odrpg : opponentDefensiveReboundsPerGame) {
			if (odrpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = odrpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (odrpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = odrpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C79-D79)");

		// Row80 - Assists Per Game
		rowhead = sheet.createRow(count + 19);
		rowhead.createCell(0).setCellValue("Assists Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData apg : assistsPerGame) {
			if (apg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = apg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (apg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = apg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F80+F81) * 0.5");
		columnf.setCellFormula("C80-D80");

		// Row81 - Opponents Assists Per Game
		rowhead = sheet.createRow(count + 20);
		rowhead.createCell(0).setCellValue("Opponents Assists Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oapg : opponentAssistsPerGame) {
			if (oapg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = oapg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (oapg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oapg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C81-D81)");

		// Row82 - Turnovers Per Game
		rowhead = sheet.createRow(count + 21);
		rowhead.createCell(0).setCellValue("Turnovers Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpg : turnoversPerGame) {
			if (tpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = tpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (tpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = tpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F82+F83) * 0.5");
		columnf.setCellFormula("-(C82-D82)");

		// Row83 - Opponents Turnovers Per Game
		rowhead = sheet.createRow(count + 22);
		rowhead.createCell(0).setCellValue("Opponents Turnovers Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpg : opponentTurnoversPerGame) {
			if (otpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = otpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (otpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = otpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("C83-D83");

		// Row84 - Personal Fouls Per Game
		rowhead = sheet.createRow(count + 23);
		rowhead.createCell(0).setCellValue("Personal Fouls Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData pfpg : personalFoulsPerGame) {
			if (pfpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = pfpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (pfpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = pfpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F84+F85) * 0.4");
		columnf.setCellFormula("-(C84-D84)");

		// Row85 - Opponent Personal Fouls Per Game
		rowhead = sheet.createRow(count + 24);
		rowhead.createCell(0).setCellValue("Opponent Personal Fouls Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData opfpg : oppoenentPersonalFoulsPerGame) {
			if (opfpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = opfpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (opfpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = opfpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("C85-D85");

		// Row86 - Extra Chances Per Game
		rowhead = sheet.createRow(count + 25);
		rowhead.createCell(0).setCellValue("Extra Chances Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ecpg : extraChancesPerGame) {
			if (ecpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ecpg.getField4();
				columnc.setCellFormula(efp1 + " * F89");
			} else if (ecpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ecpg.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("F86 * 0.05");
		columnf.setCellFormula("C86-D86");

		// Row87 - Schedule Strength By Other
		rowhead = sheet.createRow(count + 26);
		rowhead.createCell(0).setCellValue("Schedule Strength By Other");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ssbo : scheduleStrengthByOther) {
			if (kpd.getRoadTeam().equals(ssbo.getField2())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ssbo.getField4();
				columnc.setCellFormula(efp1);
			} else if (kpd.getHomeTeam().equals(ssbo.getField2())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ssbo.getField4();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row88
		rowhead = sheet.createRow(count + 27);
		HSSFCell col0 = rowhead.createCell(0);
		col0.setCellType(CellType.STRING);
		col0.setCellValue("");

		// Row89
		rowhead = sheet.createRow(count + 28);
		col0 = rowhead.createCell(0);
		col0.setCellType(CellType.STRING);
		col0.setCellValue("Home Team Spread");
		HSSFCell col1 = rowhead.createCell(1);
		col1.setCellType(CellType.STRING);
		col1.setCellValue("");
		columnc = rowhead.createCell(2);
		columnc.setCellType(CellType.STRING);
		columnc.setCellValue("");
		columnd = rowhead.createCell(3);
		columnd.setCellType(CellType.FORMULA);
		columnd.setCellFormula("SUM(E62:E87) - " + siteType);
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columnf.setCellType(CellType.FORMULA);
		columnf.setCellFormula("D69/C69");
	}

	/**
	 * 
	 * @param percentStyle
	 * @param sheet
	 * @param n
	 * @param teamOne
	 * @param teamTwo
	 * @param siteType
	 */
	public void createNcaabGiantKillersModelForTeam(HSSFCellStyle percentStyle, HSSFSheet sheet, KenPomData kpd, String teamOne, String teamTwo, int siteType) {
		int count = 90;
		HSSFRow rowhead = sheet.createRow(count);
		rowhead.createCell(0).setCellValue("Giant Killers");
		rowhead.createCell(1).setCellValue("");
		rowhead.createCell(2).setCellValue(teamOne);
		rowhead.createCell(3).setCellValue(teamTwo);

		// Row2 - Effective FG %
		rowhead = sheet.createRow(count + 1);
		rowhead.createCell(0).setCellValue("Effective FG %");
		rowhead.createCell(1).setCellValue("");
		HSSFCell columnc = rowhead.createCell(2);
		HSSFCell columnd = rowhead.createCell(3);
		float efpint1 = 0;
		float efpint2 = 0;
		for (TeamRankingsData efp : effectiveFGPct) {
			if (efp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = efp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (efp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = efp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		HSSFCell columne = rowhead.createCell(4);
		HSSFCell columnf = rowhead.createCell(5);
		HSSFCell columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F92*G92");
		columnf.setCellFormula("C92-D92");
		columng.setCellValue(60);

		// Row3 - Opponent Effective FG %
		rowhead = sheet.createRow(count + 2);
		rowhead.createCell(0).setCellValue("Opponent Effective FG %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oefp : opponentEffectiveFGPct) {
			if (oefp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = oefp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (oefp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = oefp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F93*G93");
		columnf.setCellFormula("-(C93-D93)");
		columng.setCellValue(80);

		// Row4 - 2 Point %
		rowhead = sheet.createRow(count + 3);
		rowhead.createCell(0).setCellValue("2 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpp : twoPointPct) {
			if (tpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = tpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (tpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = tpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row5 - Opponent 2 Point %
		rowhead = sheet.createRow(count + 4);
		rowhead.createCell(0).setCellValue("Opponent 2 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpp : opponentTwoPointPct) {
			if (otpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = otpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (otpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = otpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row6 - 3 Point %
		rowhead = sheet.createRow(count + 5);
		rowhead.createCell(0).setCellValue("3 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpp : threePointPct) {
			if (tpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = tpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (tpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = tpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F96*G96");
		columnf.setCellFormula("C96-D96");
		columng.setCellValue(20);

		// Row7 - Opponent 3 Point %
		rowhead = sheet.createRow(count + 6);
		rowhead.createCell(0).setCellValue("Opponent 3 Point %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpp : opponentThreePointPct) {
			if (otpp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = otpp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (otpp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = otpp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row8 - Free Throw %
		rowhead = sheet.createRow(count + 7);
		rowhead.createCell(0).setCellValue("Free Throw %");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ftp : freeThrowPct) {
			if (ftp.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellStyle(percentStyle);
				String efp1 = ftp.getField3();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
				columnc.setCellValue(efpint1);
			} else if (ftp.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellStyle(percentStyle);
				String efp2 = ftp.getField3();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columng = rowhead.createCell(6);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columng.setCellType(CellType.NUMERIC);
		columne.setCellFormula("F98*G98");
		columnf.setCellFormula("C98-D98");
		columng.setCellValue(12);

		// Row9 - Possessions Per Game
		rowhead = sheet.createRow(count + 8);
		rowhead.createCell(0).setCellValue("Possessions Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ppg : possessionsPerGame) {
			if (ppg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.NUMERIC);
				String efp1 = ppg.getField3();
				efpint1 = Float.parseFloat(efp1);
				columnc.setCellValue(efpint1);
			} else if (ppg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ppg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row10 - Offensive Efficiency
		rowhead = sheet.createRow(count + 9);
		rowhead.createCell(0).setCellValue("Offensive Efficiency");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oe : offensiveEfficiency) {
			if (oe.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.NUMERIC);
				String efp1 = oe.getField3();
				efpint1 = Float.parseFloat(efp1);
				columnc.setCellValue(efpint1);
			} else if (oe.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oe.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row11
		rowhead = sheet.createRow(count + 10);
		rowhead.createCell(0).setCellValue("");

		// Row12 - Steals Per Game
		rowhead = sheet.createRow(count + 11);
		rowhead.createCell(0).setCellValue("Steals Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData spg : stealsPerGame) {
			if (spg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = spg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (spg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = spg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F102+F103) * 0.5");
		columnf.setCellFormula("C102-D102");

		// Row13 - Opponents Steals Per Game
		rowhead = sheet.createRow(count + 12);
		rowhead.createCell(0).setCellValue("Opponents Steals Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ospg : opponentStealsPerGame) {
			if (ospg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ospg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (ospg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp1 = ospg.getField3();
				efpint1 = Float.parseFloat(efp1);
				columnd.setCellValue(efpint1);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C103-D103)");

		// Row14 - Blocks Per Game
		rowhead = sheet.createRow(count + 13);
		rowhead.createCell(0).setCellValue("Blocks Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData bpg : blocksPerGame) {
			if (bpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = bpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (bpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = bpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F104+F105) * 0.4");
		columnf.setCellFormula("C104-D104");

		// Row15 - Opponents Blocks Per Game
		rowhead = sheet.createRow(count + 14);
		rowhead.createCell(0).setCellValue("Opponents Blocks Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData obpg : opponentBlocksPerGame) {
			if (obpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = obpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (obpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = obpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C105-D105)");

		// Row16 - Offensive Rebounds Per Game
		rowhead = sheet.createRow(count + 15);
		rowhead.createCell(0).setCellValue("Offensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData orpg : offensiveReboundsPerGame) {
			if (orpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = orpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (orpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = orpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F106+F107) * 1");
		columnf.setCellFormula("C106-D106");

		// Row17 - Opponent Offensive Rebounds Per Game
		rowhead = sheet.createRow(count + 16);
		rowhead.createCell(0).setCellValue("Opponent Offensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oorpg : oponentOffensiveReboundsPerGame) {
			if (oorpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = oorpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (oorpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oorpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C107-D107)");

		// Row18 - Defensive Rebounds Per Game
		rowhead = sheet.createRow(count + 17);
		rowhead.createCell(0).setCellValue("Defensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData drpg : defensiveReboundsPerGame) {
			if (drpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = drpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (drpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = drpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F108+F109) * 0.25");
		columnf.setCellFormula("C108-D108");

		// Row19 - Opponent Defensive Rebounds Per Game
		rowhead = sheet.createRow(count + 18);
		rowhead.createCell(0).setCellValue("Opponent Defensive Rebounds Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData odrpg : opponentDefensiveReboundsPerGame) {
			if (odrpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = odrpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (odrpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = odrpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C109-D109)");

		// Row20 - Assists Per Game
		rowhead = sheet.createRow(count + 19);
		rowhead.createCell(0).setCellValue("Assists Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData apg : assistsPerGame) {
			if (apg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = apg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (apg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = apg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F110+F111) * 0.5");
		columnf.setCellFormula("C110-D110");

		// Row21 - Opponents Assists Per Game
		rowhead = sheet.createRow(count + 20);
		rowhead.createCell(0).setCellValue("Opponents Assists Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData oapg : opponentAssistsPerGame) {
			if (oapg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = oapg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (oapg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = oapg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("-(C111-D111)");

		// Row22 - Turnovers Per Game
		rowhead = sheet.createRow(count + 21);
		rowhead.createCell(0).setCellValue("Turnovers Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData tpg : turnoversPerGame) {
			if (tpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = tpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (tpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = tpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F112+F113) * 1");
		columnf.setCellFormula("-(C112-D112)");

		// Row23 - Opponents Turnovers Per Game
		rowhead = sheet.createRow(count + 22);
		rowhead.createCell(0).setCellValue("Opponents Turnovers Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData otpg : opponentTurnoversPerGame) {
			if (otpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = otpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (otpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = otpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("(C113-D113)");

		// Row24 - Personal Fouls Per Game
		rowhead = sheet.createRow(count + 23);
		rowhead.createCell(0).setCellValue("Personal Fouls Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData pfpg : personalFoulsPerGame) {
			if (pfpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = pfpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (pfpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = pfpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("(F114+F115) * 0.4");
		columnf.setCellFormula("-(C114-D114)");

		// Row25 - Opponent Personal Fouls Per Game
		rowhead = sheet.createRow(count + 24);
		rowhead.createCell(0).setCellValue("Opponent Personal Fouls Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData opfpg : oppoenentPersonalFoulsPerGame) {
			if (opfpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = opfpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (opfpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = opfpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.STRING);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellValue("");
		columnf.setCellFormula("C115-D115");

		// Row26 - Extra Chances Per Game
		rowhead = sheet.createRow(count + 25);
		rowhead.createCell(0).setCellValue("Extra Chances Per Game");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ecpg : extraChancesPerGame) {
			if (ecpg.getField2().equals(kpd.getRoadTeam())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ecpg.getField3();
				columnc.setCellFormula(efp1 + " * F119");
			} else if (ecpg.getField2().equals(kpd.getHomeTeam())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ecpg.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columne.setCellType(CellType.FORMULA);
		columnf.setCellType(CellType.FORMULA);
		columne.setCellFormula("F116 * 0.05");
		columnf.setCellFormula("C116-D116");

		// Row27 - Schedule Strength By Other
		rowhead = sheet.createRow(count + 26);
		rowhead.createCell(0).setCellValue("Schedule Strength By Other");
		rowhead.createCell(1).setCellValue("");
		columnc = rowhead.createCell(2);
		columnd = rowhead.createCell(3);
		for (TeamRankingsData ssbo : scheduleStrengthByOther) {
			if (kpd.getRoadTeam().equals(ssbo.getField2())) {
				columnc.setCellType(CellType.FORMULA);
				String efp1 = ssbo.getField3();
				columnc.setCellFormula(efp1);
			} else if (kpd.getHomeTeam().equals(ssbo.getField2())) {
				columnd.setCellType(CellType.NUMERIC);
				String efp2 = ssbo.getField3();
				efpint2 = Float.parseFloat(efp2);
				columnd.setCellValue(efpint2);
			}
		}

		// Row28
		rowhead = sheet.createRow(count + 27);
		HSSFCell col0 = rowhead.createCell(0);
		col0.setCellType(CellType.STRING);
		col0.setCellValue("");

		// Row29
		rowhead = sheet.createRow(count + 28);
		col0 = rowhead.createCell(0);
		col0.setCellType(CellType.STRING);
		col0.setCellValue("Home Team Spread");
		HSSFCell col1 = rowhead.createCell(1);
		col1.setCellType(CellType.STRING);
		col1.setCellValue("");
		columnc = rowhead.createCell(2);
		columnc.setCellType(CellType.STRING);
		columnc.setCellValue("");
		columnd = rowhead.createCell(3);
		columnd.setCellType(CellType.FORMULA);
		columnd.setCellFormula("SUM(E92:E117) - D117 + C117 - " + siteType);
		columne = rowhead.createCell(4);
		columnf = rowhead.createCell(5);
		columnf.setCellType(CellType.FORMULA);
		columnf.setCellFormula("D99/C99");
	}
	
	/**
	 * 
	 * @param workbook
	 * @return
	 */
	public HSSFWorkbook setupNcaabExcel(HSSFWorkbook workbook) {
		try {
			HSSFSheet sheet = null;
			HSSFSheet sheet2 = null;

			// Create the worksheet
			try {
				sheet = workbook.createSheet("Data Entry Sheet");
				sheet2 = workbook.createSheet("Data Entry Sheet 2");
			} catch (java.lang.IllegalArgumentException iae) {
				LOGGER.error(iae);
			}

			HSSFRow rowhead = sheet.createRow(0);
			rowhead = sheet.createRow(1);
			setupHeaderNameRows(rowhead);

			rowhead = sheet.createRow(2);
			setupHeadersRows(rowhead);

			rowhead = sheet.createRow(3);
			rowhead = sheet.createRow(4);

			final HSSFCellStyle style = workbook.createCellStyle();
			style.setDataFormat(workbook.createDataFormat().getFormat("##.##%"));

			int count = 0;
			LOGGER.error("effectiveFGPct: " + effectiveFGPct);

			for (TeamRankingsData dc : effectiveFGPct) {
				rowhead = sheet.createRow(count + 5);
				setupRowAsPercentage(style, workbook, rowhead, count, effectiveFGPct, 0);
				rowhead.createCell(8).setCellValue("");
				setupRowAsPercentage(style, workbook, rowhead, count, opponentEffectiveFGPct, 9);
				rowhead.createCell(17).setCellValue("");
				setupRowAsPercentage(style, workbook, rowhead, count, twoPointPct, 18);
				rowhead.createCell(26).setCellValue("");
				setupRowAsPercentage(style, workbook, rowhead, count, opponentTwoPointPct, 27);
				rowhead.createCell(35).setCellValue("");
				setupRowAsPercentage(style, workbook, rowhead, count, threePointPct, 36);
				rowhead.createCell(44).setCellValue("");
				setupRowAsPercentage(style, workbook, rowhead, count, opponentThreePointPct, 45);
				rowhead.createCell(53).setCellValue("");
				setupRowAsPercentage(style, workbook, rowhead, count, freeThrowPct, 54);
				rowhead.createCell(62).setCellValue("");
				setupRow(style, workbook, rowhead, count, possessionsPerGame, 63);
				rowhead.createCell(71).setCellValue("");
				setupRow(style, workbook, rowhead, count, offensiveEfficiency, 72);
				rowhead.createCell(80).setCellValue("");
				setupRow(style, workbook, rowhead, count, blocksPerGame, 81);
				rowhead.createCell(89).setCellValue("");
				setupRow(style, workbook, rowhead, count, opponentBlocksPerGame, 90);
				rowhead.createCell(98).setCellValue("");
				setupRow(style, workbook, rowhead, count, stealsPerGame, 99);
				rowhead.createCell(107).setCellValue("");
				setupRow(style, workbook, rowhead, count, opponentStealsPerGame, 108);
				rowhead.createCell(116).setCellValue("");
				setupRow(style, workbook, rowhead, count, offensiveReboundsPerGame, 117);
				rowhead.createCell(125).setCellValue("");
				setupRow(style, workbook, rowhead, count, oponentOffensiveReboundsPerGame, 126);
				rowhead.createCell(134).setCellValue("");
				setupRow(style, workbook, rowhead, count, assistsPerGame, 135);
				rowhead.createCell(143).setCellValue("");
				setupRow(style, workbook, rowhead, count, opponentAssistsPerGame, 144);
				rowhead.createCell(152).setCellValue("");
				setupRow(style, workbook, rowhead, count, turnoversPerGame, 153);
				rowhead.createCell(161).setCellValue("");
				setupRow(style, workbook, rowhead, count, opponentTurnoversPerGame, 162);
				rowhead.createCell(170).setCellValue("");
				setupRow(style, workbook, rowhead, count, personalFoulsPerGame, 171);
				rowhead.createCell(179).setCellValue("");
				setupRow(style, workbook, rowhead, count, oppoenentPersonalFoulsPerGame, 180);
				rowhead.createCell(188).setCellValue("");
				setupRow(style, workbook, rowhead, count, defensiveReboundsPerGame, 189);
				rowhead.createCell(197).setCellValue("");
				setupRow(style, workbook, rowhead, count, opponentDefensiveReboundsPerGame, 198);
				rowhead.createCell(206).setCellValue("");
				setupRow(style, workbook, rowhead, count, extraChancesPerGame, 207);
				rowhead.createCell(215).setCellValue("");
				setupSosRow(style, workbook, rowhead, count, scheduleStrengthByOther, 216);
				rowhead.createCell(221).setCellValue("");
				setupSagarinRow(style, workbook, rowhead, count, sagarinRatings, 222);
				count++;
			}

			HSSFRow rowhead2 = sheet2.createRow(0);
			rowhead2 = sheet2.createRow(1);
			setupHeaderNameRows2(rowhead2);

			rowhead2 = sheet2.createRow(2);
			setupHeadersRows2(rowhead2);

			rowhead2 = sheet2.createRow(3);
			rowhead2 = sheet2.createRow(4);

			count = 0;
			LOGGER.error("fgMadePerGame.size(): " + fgMadePerGame.size());
			for (TeamRankingsData dc : fgMadePerGame) {
				rowhead2 = sheet2.createRow(count + 5);
				setupRow(style, workbook, rowhead2, count, fgMadePerGame, 0);
				rowhead2.createCell(8).setCellValue("");
				setupRow(style, workbook, rowhead2, count, fgAttemptedPerGame, 9);
				rowhead2.createCell(17).setCellValue("");
				setupRow(style, workbook, rowhead2, count, threeFgMadePerGame, 18);
				rowhead2.createCell(26).setCellValue("");
				setupRow(style, workbook, rowhead2, count, threeFgAttemptedPerGame, 27);
				rowhead2.createCell(35).setCellValue("");
				setupRow(style, workbook, rowhead2, count, ftMadePerGame, 36);
				rowhead2.createCell(44).setCellValue("");
				setupRow(style, workbook, rowhead2, count, ftAttemptedPerGame, 45);
				rowhead2.createCell(53).setCellValue("");
				setupSagarinRow(style, workbook, rowhead2, count, sagarinRatings, 54);
				count++;
			}
		} catch (Throwable t) {
			LOGGER.error("Exception", t);
		}

		return workbook;
	}

	/**
	 * 
	 * @param rowhead
	 */
	private void setupHeaderNameRows(HSSFRow rowhead) {
		setupHeaderName(rowhead, 0, "Effective FG %");
		rowhead.createCell(8).setCellValue("");
		setupHeaderName(rowhead, 9, "Opponent Effective FG %");
		rowhead.createCell(17).setCellValue("");
		setupHeaderName(rowhead, 18, "2 Point %");
		rowhead.createCell(26).setCellValue("");
		setupHeaderName(rowhead, 27, "Opponent 2 Point %");
		rowhead.createCell(35).setCellValue("");
		setupHeaderName(rowhead, 36, "3 Point %");
		rowhead.createCell(44).setCellValue("");
		setupHeaderName(rowhead, 45, "Opponent 3 Point %");
		rowhead.createCell(53).setCellValue("");
		setupHeaderName(rowhead, 54, "Free Throw %");
		rowhead.createCell(62).setCellValue("");
		setupHeaderName(rowhead, 63, "Possessions Per Game");
		rowhead.createCell(71).setCellValue("");
		setupHeaderName(rowhead, 72, "Offensive Efficiency Rating");
		rowhead.createCell(80).setCellValue("");
		setupHeaderName(rowhead, 81, "Blocks per Game");
		rowhead.createCell(89).setCellValue("");
		setupHeaderName(rowhead, 90, "Opponents Blocks per Game");
		rowhead.createCell(98).setCellValue("");
		setupHeaderName(rowhead, 99, "Steals per Game");
		rowhead.createCell(107).setCellValue("");
		setupHeaderName(rowhead, 108, "Opponent Steals per Game");
		rowhead.createCell(116).setCellValue("");
		setupHeaderName(rowhead, 117, "Offensive Rebounds per Game");
		rowhead.createCell(125).setCellValue("");
		setupHeaderName(rowhead, 126, "Opponent Offensive Rebounds per Game");
		rowhead.createCell(134).setCellValue("");
		setupHeaderName(rowhead, 135, "Assists per Game");
		rowhead.createCell(143).setCellValue("");
		setupHeaderName(rowhead, 144, "Opponent Assists per Game");
		rowhead.createCell(152).setCellValue("");
		setupHeaderName(rowhead, 153, "Turnovers per Game");
		rowhead.createCell(161).setCellValue("");
		setupHeaderName(rowhead, 162, "Opponent Turnovers per Game");
		rowhead.createCell(170).setCellValue("");
		setupHeaderName(rowhead, 171, "Personal Fouls per Game");
		rowhead.createCell(179).setCellValue("");
		setupHeaderName(rowhead, 180, "Opponent Personal Fouls per Game");
		rowhead.createCell(188).setCellValue("");
		setupHeaderName(rowhead, 189, "Defensive Rebounds per Game");
		rowhead.createCell(197).setCellValue("");
		setupHeaderName(rowhead, 198, "Opponent Defensive Rebounds per Game");
		rowhead.createCell(206).setCellValue("");
		setupHeaderName(rowhead, 207, "Extra Scoring Chances per Game");
		rowhead.createCell(215).setCellValue("");
		setupHeaderName(rowhead, 216, "Strength of Schedule");
		rowhead.createCell(221).setCellValue("");
		setupSagarinHeaderName(rowhead, 222, "Sagarin Ratings");
	}

	/**
	 * 
	 * @param rowhead
	 */
	private void setupHeaderNameRows2(HSSFRow rowhead) {
		setupHeaderName(rowhead, 0, "FG Made per Game");
		rowhead.createCell(8).setCellValue("");
		setupHeaderName(rowhead, 9, "FG Attempted per Game");
		rowhead.createCell(17).setCellValue("");
		setupHeaderName(rowhead, 18, "3pt FG Made per Game");
		rowhead.createCell(26).setCellValue("");
		setupHeaderName(rowhead, 27, "3pt FG Attempted per Game");
		rowhead.createCell(35).setCellValue("");
		setupHeaderName(rowhead, 36, "FT Made per Game");
		rowhead.createCell(44).setCellValue("");
		setupHeaderName(rowhead, 45, "FT Attempted per Game");
	}

	/**
	 * 
	 * @param rowhead
	 * @param columStart
	 * @param name
	 */
	public void setupHeaderName(HSSFRow rowhead, int columStart, String name) {
		// Rank Team 2017 Last 3 Last 1 Home Away 2016
		final HSSFCell column1 = rowhead.createCell(columStart);
		column1.setCellType(CellType.STRING);
		column1.setCellValue("");

		final HSSFCell column2 = rowhead.createCell(columStart + 1);
		column2.setCellType(CellType.STRING);
		column2.setCellValue("");

		final HSSFCell column3 = rowhead.createCell(columStart + 2);
		column3.setCellType(CellType.STRING);
		column3.setCellValue("");

		final HSSFCell column4 = rowhead.createCell(columStart + 3);
		column4.setCellType(CellType.STRING);
		column4.setCellValue(name);

		final HSSFCell column5 = rowhead.createCell(columStart + 4);
		column5.setCellType(CellType.STRING);
		column5.setCellValue("");

		final HSSFCell column6 = rowhead.createCell(columStart + 5);
		column6.setCellType(CellType.STRING);
		column6.setCellValue("");

		final HSSFCell column7 = rowhead.createCell(columStart + 6);
		column7.setCellType(CellType.STRING);
		column7.setCellValue("");

		final HSSFCell column8 = rowhead.createCell(columStart + 7);
		column8.setCellType(CellType.STRING);
		column8.setCellValue("");
	}

	/**
	 * 
	 * @param rowhead
	 * @param columStart
	 * @param name
	 */
	public void setupSagarinHeaderName(HSSFRow rowhead, int columStart, String name) {
		// Rank Team 2017 Last 3 Last 1 Home Away 2016
		final HSSFCell column1 = rowhead.createCell(columStart);
		column1.setCellType(CellType.STRING);
		column1.setCellValue("");

		final HSSFCell column2 = rowhead.createCell(columStart + 1);
		column2.setCellType(CellType.STRING);
		column2.setCellValue(name);

		final HSSFCell column3 = rowhead.createCell(columStart + 2);
		column3.setCellType(CellType.STRING);
		column3.setCellValue("");
	}

	/**
	 * 
	 * @param rowhead
	 */
	private void setupHeadersRows(HSSFRow rowhead) {
		setupHeaders(rowhead, 0);
		rowhead.createCell(8).setCellValue("");
		setupHeaders(rowhead, 9);
		rowhead.createCell(17).setCellValue("");
		setupHeaders(rowhead, 18);
		rowhead.createCell(26).setCellValue("");
		setupHeaders(rowhead, 27);
		rowhead.createCell(35).setCellValue("");
		setupHeaders(rowhead, 36);
		rowhead.createCell(44).setCellValue("");
		setupHeaders(rowhead, 45);
		rowhead.createCell(53).setCellValue("");
		setupHeaders(rowhead, 54);
		rowhead.createCell(62).setCellValue("");
		setupHeaders(rowhead, 63);
		rowhead.createCell(71).setCellValue("");
		setupHeaders(rowhead, 72);
		rowhead.createCell(80).setCellValue("");
		setupHeaders(rowhead, 81);
		rowhead.createCell(89).setCellValue("");
		setupHeaders(rowhead, 90);
		rowhead.createCell(98).setCellValue("");
		setupHeaders(rowhead, 99);
		rowhead.createCell(107).setCellValue("");
		setupHeaders(rowhead, 108);
		rowhead.createCell(116).setCellValue("");
		setupHeaders(rowhead, 117);
		rowhead.createCell(125).setCellValue("");
		setupHeaders(rowhead, 126);
		rowhead.createCell(134).setCellValue("");
		setupHeaders(rowhead, 135);
		rowhead.createCell(143).setCellValue("");
		setupHeaders(rowhead, 144);
		rowhead.createCell(152).setCellValue("");
		setupHeaders(rowhead, 153);
		rowhead.createCell(161).setCellValue("");
		setupHeaders(rowhead, 162);
		rowhead.createCell(170).setCellValue("");
		setupHeaders(rowhead, 171);
		rowhead.createCell(179).setCellValue("");
		setupHeaders(rowhead, 180);
		rowhead.createCell(188).setCellValue("");
		setupHeaders(rowhead, 189);
		rowhead.createCell(197).setCellValue("");
		setupHeaders(rowhead, 198);
		rowhead.createCell(206).setCellValue("");
		setupHeaders(rowhead, 207);
		rowhead.createCell(215).setCellValue("");
		setupSoSHeaders(rowhead, 216);
		rowhead.createCell(221).setCellValue("");
		setupSagarinHeaders(rowhead, 222);
	}

	/**
	 * 
	 * @param rowhead
	 */
	private void setupHeadersRows2(HSSFRow rowhead) {
		setupHeaders(rowhead, 0);
		rowhead.createCell(8).setCellValue("");
		setupHeaders(rowhead, 9);
		rowhead.createCell(17).setCellValue("");
		setupHeaders(rowhead, 18);
		rowhead.createCell(26).setCellValue("");
		setupHeaders(rowhead, 27);
		rowhead.createCell(35).setCellValue("");
		setupHeaders(rowhead, 36);
		rowhead.createCell(44).setCellValue("");
		setupHeaders(rowhead, 45);
	}

	/**
	 * 
	 * @param rowhead
	 * @param columStart
	 */
	public void setupHeaders(HSSFRow rowhead, int columStart) {
		// Rank Team 2017 Last 3 Last 1 Home Away 2016
		final HSSFCell column1 = rowhead.createCell(columStart);
		column1.setCellType(CellType.STRING);
		column1.setCellValue("Rank");

		final HSSFCell column2 = rowhead.createCell(columStart + 1);
		column2.setCellType(CellType.STRING);
		column2.setCellValue("Team");

		final HSSFCell column3 = rowhead.createCell(columStart + 2);
		column3.setCellType(CellType.STRING);
		column3.setCellValue(" 2017 ");

		final HSSFCell column4 = rowhead.createCell(columStart + 3);
		column4.setCellType(CellType.STRING);
		column4.setCellValue("Last 3");

		final HSSFCell column5 = rowhead.createCell(columStart + 4);
		column5.setCellType(CellType.STRING);
		column5.setCellValue("Last 1");

		final HSSFCell column6 = rowhead.createCell(columStart + 5);
		column6.setCellType(CellType.STRING);
		column6.setCellValue("Home");

		final HSSFCell column7 = rowhead.createCell(columStart + 6);
		column7.setCellType(CellType.STRING);
		column7.setCellValue("Away");

		final HSSFCell column8 = rowhead.createCell(columStart + 7);
		column8.setCellType(CellType.STRING);
		column8.setCellValue(" 2016 ");
	}

	/**
	 * 
	 * @param rowhead
	 * @param columStart
	 */
	public void setupSoSHeaders(HSSFRow rowhead, int columStart) {
		// Rank Team 2017 Last 3 Last 1 Home Away 2016
		final HSSFCell column1 = rowhead.createCell(columStart);
		column1.setCellType(CellType.STRING);
		column1.setCellValue("Rank");

		final HSSFCell column2 = rowhead.createCell(columStart + 1);
		column2.setCellType(CellType.STRING);
		column2.setCellValue("Team");

		final HSSFCell column3 = rowhead.createCell(columStart + 2);
		column3.setCellType(CellType.STRING);
		column3.setCellValue("Rating");

		final HSSFCell column4 = rowhead.createCell(columStart + 3);
		column4.setCellType(CellType.STRING);
		column4.setCellValue("High");

		final HSSFCell column5 = rowhead.createCell(columStart + 4);
		column5.setCellType(CellType.STRING);
		column5.setCellValue("Low");

		final HSSFCell column6 = rowhead.createCell(columStart + 5);
		column6.setCellType(CellType.STRING);
		column6.setCellValue("Last");
	}
	
	/**
	 * 
	 * @param rowhead
	 * @param columStart
	 */
	public void setupSagarinHeaders(HSSFRow rowhead, int columStart) {
		// Rank Team 2017 Last 3 Last 1 Home Away 2016
		final HSSFCell column1 = rowhead.createCell(columStart);
		column1.setCellType(CellType.STRING);
		column1.setCellValue("Rank");

		final HSSFCell column2 = rowhead.createCell(columStart + 1);
		column2.setCellType(CellType.STRING);
		column2.setCellValue("Team");

		final HSSFCell column3 = rowhead.createCell(columStart + 2);
		column3.setCellType(CellType.STRING);
		column3.setCellValue("Rating");
	}

	/**
	 * 
	 * @param style
	 * @param workbook
	 * @param rowhead
	 * @param rowStart
	 * @param TeamRankingsData
	 * @param columStart
	 */
	public void setupRowAsPercentage(HSSFCellStyle style, HSSFWorkbook workbook, HSSFRow rowhead, int rowStart, List<TeamRankingsData> TeamRankingsData, int columStart) {
		int counter = 0;
		for (TeamRankingsData dc : TeamRankingsData) {
			if (counter == rowStart) {
				
				final HSSFCell column1 = rowhead.createCell(columStart);
				column1.setCellType(CellType.NUMERIC);
				column1.setCellValue(Integer.parseInt(dc.getField1()));

				final HSSFCell column2 = rowhead.createCell(columStart+1);
				column2.setCellType(CellType.STRING);
				column2.setCellValue(dc.getField2());

				final HSSFCell column3 = rowhead.createCell(columStart+2);
				column3.setCellStyle(style);
				String f3 = dc.getField3().replaceAll("%", "");
				f3 = f3.replaceAll("\\.", "");
				f3 = f3.replaceAll("--", "0");
				f3 = "0." + f3;
				column3.setCellValue(Float.parseFloat(f3));

				final HSSFCell column4 = rowhead.createCell(columStart+3);
				column4.setCellStyle(style);
				String f4 = dc.getField4().replaceAll("%", "");
				f4 = f4.replaceAll("\\.", "");
				f4 = f4.replaceAll("--", "0");
				f4 = "0." + f4;
				column4.setCellValue(Float.parseFloat(f4));

				final HSSFCell column5 = rowhead.createCell(columStart+4);
				column5.setCellStyle(style);
				String f5 = dc.getField5().replaceAll("%", "");
				f5 = f5.replaceAll("\\.", "");
				f5 = f5.replaceAll("--", "0");
				f5 = "0." + f5;
				column5.setCellValue(Float.parseFloat(f5));

				final HSSFCell column6 = rowhead.createCell(columStart+5);
				column6.setCellStyle(style);
				String f6 = dc.getField6().replaceAll("%", "");
				f6 = f6.replaceAll("\\.", "");
				f6 = f6.replaceAll("--", "0");
				f6 = "0." + f6;
				column6.setCellValue(Float.parseFloat(f6));

				final HSSFCell column7 = rowhead.createCell(columStart+6);
				column7.setCellStyle(style);
				String f7 = dc.getField7().replaceAll("%", "");
				f7 = f7.replaceAll("\\.", "");
				f7 = f7.replaceAll("--", "0");
				f7 = "0." + f7;
				column7.setCellValue(Float.parseFloat(f7));

				final HSSFCell column8 = rowhead.createCell(columStart+7);
				column8.setCellStyle(style);
				String f8 = dc.getField8().replaceAll("%", "");
				f8 = f8.replaceAll("\\.", "");
				f8 = f8.replaceAll("--", "0");
				f8 = "0." + f8;
				column8.setCellValue(Float.parseFloat(f8));
			}
			counter++;
		}
	}

	/**
	 * 
	 * @param style
	 * @param workbook
	 * @param rowhead
	 * @param rowStart
	 * @param TeamRankingsData
	 * @param columStart
	 */
	public void setupRow(HSSFCellStyle style, HSSFWorkbook workbook, HSSFRow rowhead, int rowStart, List<TeamRankingsData> TeamRankingsData, int columStart) {
		int counter = 0;
		for (TeamRankingsData dc : TeamRankingsData) {
			if (counter == rowStart) {
				final HSSFCell column1 = rowhead.createCell(columStart);
				column1.setCellType(CellType.NUMERIC);
				column1.setCellValue(Integer.parseInt(dc.getField1()));

				final HSSFCell column2 = rowhead.createCell(columStart+1);
				column2.setCellType(CellType.STRING);
				column2.setCellValue(dc.getField2());

				final HSSFCell column3 = rowhead.createCell(columStart+2);
				column3.setCellType(CellType.NUMERIC);
				String f3 = dc.getField3().replaceAll("--", "0");
				column3.setCellValue(Float.parseFloat(f3));

				final HSSFCell column4 = rowhead.createCell(columStart+3);
				column4.setCellType(CellType.NUMERIC);
				String f4 = dc.getField4().replaceAll("--", "0");
				column4.setCellValue(Float.parseFloat(f4));

				final HSSFCell column5 = rowhead.createCell(columStart+4);
				column5.setCellType(CellType.NUMERIC);
				String f5 = dc.getField5().replaceAll("--", "0");
				column5.setCellValue(Float.parseFloat(f5));

				final HSSFCell column6 = rowhead.createCell(columStart+5);
				column6.setCellType(CellType.NUMERIC);
				String f6 = dc.getField6().replaceAll("--", "0");
				column6.setCellValue(Float.parseFloat(f6));

				final HSSFCell column7 = rowhead.createCell(columStart+6);
				column7.setCellType(CellType.NUMERIC);
				String f7 = dc.getField7().replaceAll("--", "0");
				column7.setCellValue(Float.parseFloat(f7));

				final HSSFCell column8 = rowhead.createCell(columStart+7);
				column8.setCellType(CellType.NUMERIC);
				String f8 = dc.getField8().replaceAll("--", "0");
				column8.setCellValue(Float.parseFloat(f8));
			}
			counter++;
		}
	}

	/**
	 * 
	 * @param style
	 * @param workbook
	 * @param rowhead
	 * @param rowStart
	 * @param TeamRankingsData
	 * @param columStart
	 */
	public void setupSosRow(HSSFCellStyle style, HSSFWorkbook workbook, HSSFRow rowhead, int rowStart, List<TeamRankingsData> TeamRankingsData, int columStart) {
		int counter = 0;
		for (TeamRankingsData dc : TeamRankingsData) {
			if (counter == rowStart) {
				final HSSFCell column1 = rowhead.createCell(columStart);
				column1.setCellType(CellType.NUMERIC);
				column1.setCellValue(Integer.parseInt(dc.getField1()));

				final HSSFCell column2 = rowhead.createCell(columStart+1);
				column2.setCellType(CellType.STRING);
				column2.setCellValue(dc.getField2());

				final HSSFCell column3 = rowhead.createCell(columStart+2);
				column3.setCellType(CellType.NUMERIC);
				String f3 = dc.getField3().replaceAll("--", "0");
				column3.setCellValue(Float.parseFloat(f3));

				final HSSFCell column4 = rowhead.createCell(columStart+3);
				column4.setCellType(CellType.NUMERIC);
				String f4 = dc.getField4().replaceAll("--", "0");
				column4.setCellValue(Float.parseFloat(f4));

				final HSSFCell column5 = rowhead.createCell(columStart+4);
				column5.setCellType(CellType.NUMERIC);
				String f5 = dc.getField5().replaceAll("--", "0");
				column5.setCellValue(Float.parseFloat(f5));

				final HSSFCell column6 = rowhead.createCell(columStart+5);
				column6.setCellType(CellType.NUMERIC);
				String f6 = dc.getField6().replaceAll("--", "0");
				column6.setCellValue(Float.parseFloat(f6));
			}
			counter++;
		}
	}

	/**
	 * 
	 * @param style
	 * @param workbook
	 * @param rowhead
	 * @param rowStart
	 * @param TeamRankingsData
	 * @param columStart
	 */
	public void setupSagarinRow(HSSFCellStyle style, HSSFWorkbook workbook, HSSFRow rowhead, int rowStart, List<SagarinData> TeamRankingsData, int columStart) {
		int counter = 0;
		for (SagarinData sd : TeamRankingsData) {
			if (counter == rowStart) {
				final HSSFCell column1 = rowhead.createCell(columStart);
				column1.setCellType(CellType.NUMERIC);
				column1.setCellValue(sd.getRank());

				final HSSFCell column2 = rowhead.createCell(columStart+1);
				column2.setCellType(CellType.STRING);
				column2.setCellValue(sd.getTeam());

				final HSSFCell column3 = rowhead.createCell(columStart+2);
				column3.setCellType(CellType.NUMERIC);
				column3.setCellValue(sd.getValue());
			}
			counter++;
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<SpreadLastThree> getNextDaySpreads() {
		LOGGER.debug("Entering getNextDaySpreads()");
		final List<SpreadLastThree> spreadLastThreeList = new ArrayList<SpreadLastThree>();

		// Setup report data
		setupReportData();

		for (KenPomData kpd : nextdaykpd) {
			if (kpd.getRoadTeam() != null && kpd.getRoadTeam().length() > 0 && 
				kpd.getHomeTeam() != null && kpd.getHomeTeam().length() > 0) {
				// Create the worksheet
				final String roadTeam = kpd.getRoadTeam().replace("'", "");
				final String homeTeam = kpd.getHomeTeam().replace("'", "");

				int siteType = -4;
				for (KenPomData kp : nextdaykpd) {
					if (kp.getHomeTeam() != null && kp.getHomeTeam().equals(homeTeam) &&
						kp.getRoadTeam() != null && kp.getRoadTeam().equals(roadTeam)) {
						siteType = kp.getSiteLocation();
					}
				}

				// Get the homespread value
				float homespread = generateNcaabLast3ModelForTeam(kpd, roadTeam, homeTeam, siteType);
				final SpreadLastThree spreadLastThree = new SpreadLastThree();
				final Integer spread = new Integer(kpd.getSpread());
				spreadLastThree.setKenpomspread(Float.parseFloat(spread.toString()));
				spreadLastThree.setRoadteam(roadTeam);
				spreadLastThree.setHometeam(homeTeam);
				spreadLastThree.setHomespread(homespread);

				spreadLastThreeList.add(spreadLastThree);
			}
		}

		LOGGER.debug("Exiting getNextDaySpreads()");
		return spreadLastThreeList;
	}

	/**
	 * 
	 */
	private void setupReportData() {
		LOGGER.debug("Entering setupReportData()");

		lkpd = REPORT_CACHE.getLkpd();
		nextdaykpd = REPORT_CACHE.getNextdaykpd();
		effectiveFGPct = REPORT_CACHE.getEffectiveFGPct();
		opponentEffectiveFGPct = REPORT_CACHE.getOpponentEffectiveFGPct();
		twoPointPct = REPORT_CACHE.getTwoPointPct();
		opponentTwoPointPct = REPORT_CACHE.getOpponentTwoPointPct();
		threePointPct = REPORT_CACHE.getThreePointPct();
		opponentThreePointPct = REPORT_CACHE.getOpponentThreePointPct();
		freeThrowPct = REPORT_CACHE.getFreeThrowPct();
		possessionsPerGame = REPORT_CACHE.getPossessionsPerGame();
		offensiveEfficiency = REPORT_CACHE.getOffensiveEfficiency();
		blocksPerGame = REPORT_CACHE.getBlocksPerGame();
		opponentBlocksPerGame = REPORT_CACHE.getOpponentBlocksPerGame();
		stealsPerGame = REPORT_CACHE.getStealsPerGame();
		opponentStealsPerGame = REPORT_CACHE.getOpponentStealsPerGame();
		offensiveReboundsPerGame = REPORT_CACHE.getOffensiveReboundsPerGame();
		oponentOffensiveReboundsPerGame = REPORT_CACHE.getOponentOffensiveReboundsPerGame();
		assistsPerGame = REPORT_CACHE.getAssistsPerGame();
		opponentAssistsPerGame = REPORT_CACHE.getOpponentAssistsPerGame();
		turnoversPerGame = REPORT_CACHE.getTurnoversPerGame();
		opponentTurnoversPerGame = REPORT_CACHE.getOpponentTurnoversPerGame();
		personalFoulsPerGame = REPORT_CACHE.getPersonalFoulsPerGame();
		oppoenentPersonalFoulsPerGame = REPORT_CACHE.getOppoenentPersonalFoulsPerGame();
		defensiveReboundsPerGame = REPORT_CACHE.getDefensiveReboundsPerGame();
		opponentDefensiveReboundsPerGame = REPORT_CACHE.getOpponentDefensiveReboundsPerGame();
		extraChancesPerGame = REPORT_CACHE.getExtraChancesPerGame();
		scheduleStrengthByOther = REPORT_CACHE.getScheduleStrengthByOther();
		fgMadePerGame = REPORT_CACHE.getFgMadePerGame();
		fgAttemptedPerGame = REPORT_CACHE.getFgAttemptedPerGame();
		threeFgMadePerGame = REPORT_CACHE.getThreeFgMadePerGame();
		threeFgAttemptedPerGame = REPORT_CACHE.getThreeFgAttemptedPerGame();
		ftMadePerGame = REPORT_CACHE.getFtMadePerGame();
		ftAttemptedPerGame = REPORT_CACHE.getFtAttemptedPerGame();
		sagarinRatings = REPORT_CACHE.getSagarinRatings();
		masseyComposite = REPORT_CACHE.getMasseyComposite();

		LOGGER.debug("Entering setupReportData()");
	}

	/**
	 * 
	 * @param kpd
	 * @param teamOne
	 * @param teamTwo
	 * @param siteType
	 */
	private float generateNcaabLast3ModelForTeam(KenPomData kpd, String teamOne, String teamTwo, int siteType) {
		float homespread = Float.parseFloat("0.0");

		// Row62 - Effective FG %
		float efpint1 = 0;
		float efpint2 = 0;
		for (TeamRankingsData efp : effectiveFGPct) {
			if (efp.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = efp.getField4();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
			} else if (efp.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = efp.getField4();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
			}
		}
		float effg = ((efpint1 - efpint2) * 60);
		LOGGER.debug("effg: " + effg);
		homespread += effg;

		// Row63 - Opponent Effective FG %
		for (TeamRankingsData oefp : opponentEffectiveFGPct) {
			if (oefp.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = oefp.getField4();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
			} else if (oefp.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = oefp.getField4();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
			}
		}
		float oppeffg= (-(efpint1 - efpint2) * 60);
		LOGGER.debug("oppeffg: " + oppeffg);
		homespread += oppeffg;

		// Row68 - Free Throw %
		for (TeamRankingsData ftp : freeThrowPct) {
			if (ftp.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = ftp.getField4();
				efp1 = efp1.replace("%", "");
				efp1 = efp1.replace(".", "");
				efpint1 = Float.parseFloat("0." + efp1);
			} else if (ftp.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = ftp.getField4();
				efp2 = efp2.replace("%", "");
				efp2 = efp2.replace(".", "");
				efpint2 = Float.parseFloat("0." + efp2);
			}
		}
		float ftpct = ((efpint1 - efpint2) * 12);
		LOGGER.debug("ftpct: " + ftpct);
		homespread += ftpct;

		float ppg1 = 0;
		float ppg2 = 0;
		for (TeamRankingsData ppg : possessionsPerGame) {
			if (ppg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = ppg.getField4();
				ppg1 = Float.parseFloat(efp1);
			} else if (ppg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = ppg.getField4();
				ppg2 = Float.parseFloat(efp2);
			}
		}
		float visitorfactor = ppg2/ppg1;

		// Row72 - Steals Per Game
		for (TeamRankingsData spg : stealsPerGame) {
			if (spg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = spg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (spg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = spg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		LOGGER.debug("steals1: " + efpint1);
		LOGGER.debug("steals2: " + efpint2);
		float set1 = efpint1 - efpint2;
		LOGGER.debug("stealsXXX: " + set1);

		// Row73 - Opponents Steals Per Game
		for (TeamRankingsData ospg : opponentStealsPerGame) {
			if (ospg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = ospg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (ospg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = ospg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		float set2 = -(efpint1 - efpint2);
		LOGGER.debug("opp steals: " + set2);

		float factor = Float.parseFloat("0.5");
		float steals = (set1 + set2) * factor;
		LOGGER.debug("steals: " + steals);
		homespread += steals;

		// Row74 - Blocks Per Game
		for (TeamRankingsData bpg : blocksPerGame) {
			if (bpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = bpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (bpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = bpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		LOGGER.debug("blocks1: " + efpint1);
		LOGGER.debug("blocks2: " + efpint2);
		set1 = efpint1 - efpint2;
		LOGGER.debug("blocksXXX: " + set1);

		// Row75 - Opponents Blocks Per Game
		for (TeamRankingsData obpg : opponentBlocksPerGame) {
			if (obpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = obpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (obpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = obpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set2 = -(efpint1 - efpint2);
		LOGGER.debug("opp blocks: " + set2);
	
		factor = Float.parseFloat("0.4");
		float blocks = (set1 + set2) * factor;
		LOGGER.debug("blocks: " + blocks);
		homespread += blocks;
		
		// Row76 - Offensive Rebounds Per Game
		for (TeamRankingsData orpg : offensiveReboundsPerGame) {
			if (orpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = orpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (orpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = orpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set1 = efpint1 - efpint2;

		// Row77 - Opponent Offensive Rebounds Per Game
		for (TeamRankingsData oorpg : oponentOffensiveReboundsPerGame) {
			if (oorpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = oorpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (oorpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = oorpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set2 = -(efpint1 - efpint2);
		factor = Float.parseFloat("0.5");
		float offrebounds = (set1 + set2) * factor;
		LOGGER.debug("offrebounds: " + offrebounds);
		homespread += offrebounds;

		// Row78 - Defensive Rebounds Per Game
		for (TeamRankingsData drpg : defensiveReboundsPerGame) {
			if (drpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = drpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (drpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = drpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set1 = efpint1 - efpint2;

		// Row79 - Opponent Defensive Rebounds Per Game
		for (TeamRankingsData odrpg : opponentDefensiveReboundsPerGame) {
			if (odrpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = odrpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (odrpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = odrpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set2 = -(efpint1 - efpint2);
		factor = Float.parseFloat("0.25");
		float defrebounds = (set1 + set2) * factor;
		LOGGER.debug("defrebounds: " + defrebounds);
		homespread += defrebounds;

		// Row80 - Assists Per Game
		for (TeamRankingsData apg : assistsPerGame) {
			if (apg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = apg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (apg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = apg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set1 = efpint1 - efpint2;

		// Row81 - Opponents Assists Per Game
		for (TeamRankingsData oapg : opponentAssistsPerGame) {
			if (oapg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = oapg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (oapg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = oapg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set2 = -(efpint1 - efpint2);
		factor = Float.parseFloat("0.5");
		float assists = (set1 + set2) * factor;
		LOGGER.debug("assists: " + assists);
		homespread += assists;

		// Row82 - Turnovers Per Game
		for (TeamRankingsData tpg : turnoversPerGame) {
			if (tpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = tpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (tpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = tpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set1 = -(efpint1 - efpint2);

		// Row83 - Opponents Turnovers Per Game
		for (TeamRankingsData otpg : opponentTurnoversPerGame) {
			if (otpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = otpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (otpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = otpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set2 = efpint1 - efpint2;
		factor = Float.parseFloat("0.5");
		float turnovers = (set1 + set2) * factor;
		LOGGER.debug("turnovers: " + turnovers);
		homespread += turnovers;

		// Row84 - Personal Fouls Per Game
		for (TeamRankingsData pfpg : personalFoulsPerGame) {
			if (pfpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = pfpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (pfpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = pfpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set1 = -(efpint1 - efpint2);

		// Row85 - Opponent Personal Fouls Per Game
		for (TeamRankingsData opfpg : oppoenentPersonalFoulsPerGame) {
			if (opfpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = opfpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (opfpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = opfpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		set2 = efpint1 - efpint2;
		factor = Float.parseFloat("0.4");
		float fouls = (set1 + set2) * factor;
		LOGGER.debug("fouls: " + fouls);
		homespread += fouls;

		// Row86 - Extra Chances Per Game
		for (TeamRankingsData ecpg : extraChancesPerGame) {
			if (ecpg.getField2().equals(kpd.getRoadTeam())) {
				String efp1 = ecpg.getField4();
				efpint1 = Float.parseFloat(efp1) * visitorfactor;
			} else if (ecpg.getField2().equals(kpd.getHomeTeam())) {
				String efp2 = ecpg.getField4();
				efpint2 = Float.parseFloat(efp2);
			}
		}
		factor = Float.parseFloat("0.05");
		float extrachances = (efpint1 - efpint2) * factor;
		LOGGER.debug("extrachances: " + extrachances);
		homespread += extrachances;
		
		LOGGER.debug("homespread BEFORE: " + homespread);
		homespread = homespread - siteType;
		LOGGER.debug("homespread AFTER: " + homespread);

		return homespread;
	}
}