package com.ticketadvantage.services.dao.sites.vegasinsider;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.site.util.WeekByDate;

/**
 * 
 * @author jmiller
 *
 */
public class VegasInsiderProcessSite extends SiteProcessor {
	private final static Logger LOGGER = Logger.getLogger(VegasInsiderProcessSite.class);
	private final static VegasInsiderParser vip = new VegasInsiderParser();

	/**
	 * 
	 */
	public VegasInsiderProcessSite() {
		super("VegasInsider", "http://www.vegasinsider.com", "", "", false, false);
		LOGGER.info("Entering VegasInsiderProcessSite()");

		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting VegasInsiderProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final VegasInsiderProcessSite vips = new VegasInsiderProcessSite();
			final EventPackage ep = vips.getNcaabGameByRotationID(307828);
			LOGGER.error("EventPackage: " + ep);

	//		final List<VegasInsiderGame> games = vips.getNcaafGameData(2018, 10, 10, false);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 2);
			cal.set(Calendar.YEAR, 2017);
			Date startDate = cal.getTime();
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 2);
			cal.set(Calendar.YEAR, 2017);
			Date endDate = cal.getTime();
//			vips.determineNbaOutcome(2017, startDate, endDate);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 * @param year
	 * @param startWeek
	 * @param endWeek
	 * @param includebowls
	 * @return
	 */
	public List<VegasInsiderGame> getNcaafGameData(Integer year, Integer startWeek, Integer endWeek, Boolean includebowls) {
		final List<VegasInsiderGame> games = new ArrayList<VegasInsiderGame>();

		try {
			for (int week = startWeek; week <= endWeek; week++) {
				LOGGER.debug("Week: " + week);
				String url = "http://www.vegasinsider.com/college-football/scoreboard/scores.cfm/week/" + week + "/season/" + year.toString();
				String xhtml = super.getSite(url);
				final List<VegasInsiderGame> tempgames = vip.getNcaafGameData(xhtml, week, year);
				for (VegasInsiderGame vig : tempgames) {
					LOGGER.debug("VegasInsiderGame: " + vig);
					games.add(vig);
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return games;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getNcaafFCSGameData() {
		final List<String> fcsTeams = new ArrayList<String>();

		try {
				Integer year = 0;
				final List<VegasInsiderGame> games = new ArrayList<VegasInsiderGame>();
				for (int y = 0; y < 2; y++) {
					if (y == 0) {
						year = 2017;
					} else {
						year = 2018;
					}
					for (int x = 1; x < 15; x++) {
						String url = "http://www.vegasinsider.com/college-football/scoreboard/scores.cfm/week/" + x + "/season/" + year.toString();
						String xhtml = super.getSite(url);
						final List<VegasInsiderGame> tempgames = vip.getNcaafGameData(xhtml, x, year);

						for (VegasInsiderGame vig : tempgames) {
							LOGGER.debug("VegasInsiderGame: " + vig);

							if (!vig.getAwayteamdata().getIsfbs()) {
								boolean didnotfind = false;
								for (String nn : fcsTeams) {
									if (nn.equals(vig.getAwayteamdata().getTeamname())) {
										didnotfind = true;
									}
								}
								if (!didnotfind) {
									fcsTeams.add(vig.getAwayteamdata().getTeamname());
								}
							}
							if (!vig.getHometeamdata().getIsfbs()) {
								boolean didnotfind = false;
								for (String nn : fcsTeams) {
									if (nn.equals(vig.getHometeamdata().getTeamname())) {
										didnotfind = true;
									}
								}
								if (!didnotfind) {
									fcsTeams.add(vig.getHometeamdata().getTeamname());
								}
							}

						}
					}
				}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return fcsTeams;
	}

	/**
	 * 
	 * @param rotationID
	 * @return
	 */
	public EventPackage getNcaabGameByRotationID(Integer rotationID) {
		EventPackage epgame = null;

		try {
			final String url = "http://www.vegasinsider.com/college-basketball/scoreboard/";
			final String xhtml = super.getSite(url);
			final Calendar cal = Calendar.getInstance();

			final List<EventPackage> tempgames = vip.getNcaabGames(xhtml, cal.get(Calendar.MONTH) + 1,
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
			for (EventPackage ep : tempgames) {
				LOGGER.error("EventPackage: " + ep);

				if (ep.getTeamone() != null && ep.getTeamone().getId() != null && ep.getTeamtwo() != null
						&& ep.getTeamtwo().getId() != null
						&& (ep.getTeamone().getId().intValue() == rotationID.intValue() || 
						    ep.getTeamtwo().getId().intValue() == rotationID.intValue())) {
					epgame = ep;
					break;
				}
			}
		} catch (Throwable t) {
			LOGGER.debug(t.getMessage(), t);
		}

		return epgame;
	}

	/**
	 * 
	 * @param seasonyear
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<VegasInsiderGame> getNcaabGameData(Integer seasonyear, Date startDate, Date endDate) {
		final List<VegasInsiderGame> games = new ArrayList<VegasInsiderGame>();

		try {
			LOGGER.error("startDate: " + startDate);
			LOGGER.error("endDate: " + endDate);
			final LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			final LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			for (LocalDate date = start; date.isBefore(end) || date.isEqual(end); date = date.plusDays(1)) {
				LOGGER.error("date: " + date);
				String url = "http://www.vegasinsider.com/college-basketball/scoreboard/scores.cfm/game_date/" + 
						date.getMonthValue() + "-" + date.getDayOfMonth() + "-" + date.getYear() + "?s=67";
				String xhtml = super.getSite(url);
				int week = WeekByDate.DetermineWeek(seasonyear, java.sql.Date.valueOf(date));
				final List<VegasInsiderGame> tempgames = vip.getNcaabGameData(xhtml, date.getMonthValue(), date.getDayOfMonth(), date.getYear());
				for (VegasInsiderGame vig : tempgames) {
					vig.setWeek(week);
					LOGGER.debug("VegasInsiderGame: " + vig);
					games.add(vig);
				}
			}
		} catch (Throwable t) {
			LOGGER.debug(t.getMessage(), t);
		}

		return games;
	}

	/**
	 * 
	 * @param seasonyear
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<VegasInsiderGame> getNbaGameData(Integer seasonyear, Date startDate, Date endDate) {
		final List<VegasInsiderGame> games = new ArrayList<VegasInsiderGame>();

		try {
			LOGGER.error("startDate: " + startDate);
			LOGGER.error("endDate: " + endDate);
			final LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			final LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			for (LocalDate date = start; date.isBefore(end) || date.isEqual(end); date = date.plusDays(1)) {
				LOGGER.error("date: " + date);
				String url = "http://www.vegasinsider.com/nba/scoreboard/scores.cfm/game_date/" + 
						date.getMonthValue() + "-" + date.getDayOfMonth() + "-" + date.getYear() + "?s=67";
				String xhtml = super.getSite(url);
				int week = WeekByDate.DetermineWeek(seasonyear, java.sql.Date.valueOf(date));
				final List<VegasInsiderGame> tempgames = vip.getNcaabGameData(xhtml, date.getMonthValue(), date.getDayOfMonth(), date.getYear());
				for (VegasInsiderGame vig : tempgames) {
					vig.setWeek(week);
					LOGGER.debug("VegasInsiderGame: " + vig);
					xhtml = super.getSite("http://www.vegasinsider.com" + vig.getLineurl());
					final VegasInsiderLineMovement vilm = vip.getLineMovement(xhtml);
					vig.setVilm(vilm);
					games.add(vig);
				}
			}
		} catch (Throwable t) {
			LOGGER.debug(t.getMessage(), t);
		}

		return games;
	}

	/**
	 * 
	 * @param seasonyear
	 * @param startDate
	 * @param endDate
	 */
	public void determineNbaOutcome(Integer seasonyear, Date startDate, Date endDate) {
		final List<VegasInsiderGame> games = new ArrayList<VegasInsiderGame>();
		int wins = 0;
		int losses = 0;
		boolean showinfo = true;

		try {
			LOGGER.error("startDate: " + startDate);
			LOGGER.error("endDate: " + endDate);
			final LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			final LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			for (LocalDate date = start; date.isBefore(end) || date.isEqual(end); date = date.plusDays(1)) {
				LOGGER.debug("date: " + date);
				String url = "http://www.vegasinsider.com/nba/scoreboard/scores.cfm/game_date/" + 
						date.getMonthValue() + "-" + date.getDayOfMonth() + "-" + date.getYear() + "?s=67";
				String xhtml = super.getSite(url);
				int week = WeekByDate.DetermineWeek(seasonyear, java.sql.Date.valueOf(date));
				final List<VegasInsiderGame> tempgames = vip.getNcaabGameData(xhtml, date.getMonthValue(), date.getDayOfMonth(), date.getYear());
				for (VegasInsiderGame vig : tempgames) {
					vig.setWeek(week);
					LOGGER.debug("VegasInsiderGame: " + vig);
					xhtml = super.getSite("http://www.vegasinsider.com" + vig.getLineurl());
					final VegasInsiderLineMovement vilm = vip.getLineMovement(xhtml);
					vig.setVilm(vilm);
					games.add(vig);
				}
			}

			int currentdaywins = 0;
			int currentdaylosses = 0;
			String currentdate = "";

			for (VegasInsiderGame vig : games) {
				final VegasInsiderLineMovement vilm = vig.getVilm();
				final List<VegasInsiderLinePoint> vilps = vilm.getLinepoints();
				String startspreadfav = "";
				String startspreadfavteam = "";
				String startspreaddog = "";
				String startspreaddogteam = "";
				String endspreadfav = "";
				String endspreadfavteam = "";
				String endspreaddog = "";
				String endspreaddogteam = "";
				String gamedate = vig.getMonth() + "-" + vig.getDay() + "-" + vig.getYear();

				if (currentdate.length() == 0) {
					currentdate = gamedate;
				} else {
					if (!gamedate.equals(currentdate)) {
						LOGGER.error("Date: " + currentdate + " wins: " + currentdaywins + " losses: " + currentdaylosses);
						currentdate = gamedate;
						currentdaywins = 0;
						currentdaylosses = 0;
					}
				}

				boolean firstfind = true;

				for (VegasInsiderLinePoint vilp : vilps) {
					if (firstfind && vilp.getMlfav() != null && vilp.getMlfav().length() > 0) {
						startspreadfav = vilp.getSpreadfav();
						startspreadfavteam = vilp.getSpreadfavteam();
						startspreaddog = vilp.getSpreaddog();
						startspreaddogteam = vilp.getSpreaddogteam();
						firstfind = false;
					}
					
					endspreadfav = vilp.getSpreadfav();
					endspreadfavteam = vilp.getSpreadfavteam();
					endspreaddog = vilp.getSpreaddog();
					endspreaddogteam = vilp.getSpreaddogteam();
				}
				
				try {
					if (startspreadfavteam.equals(endspreadfavteam)) {
						// Same team, now check how far apart
						float ssf = Float.parseFloat(startspreadfav);
						float esf = Float.parseFloat(endspreadfav);
						float delta = ssf - esf;
						
						if (delta >= 1) {
							// Means fav is trending to win
							if (vig.getAwayteamdata().getShortname().equals(startspreadfavteam)) {
								if (showinfo) {
									LOGGER.error("Date: " + vig.getMonth() + "-" + vig.getDay() + "-" + vig.getYear());
									LOGGER.error("Line: " + vig.getLine());
									LOGGER.error("LineFavorite: " + vig.getLinefavorite());
									LOGGER.error("FavTeam: " + vig.getAwayteamdata().getTeamname());
									LOGGER.error("DogTeam: " + vig.getHometeamdata().getTeamname());
									LOGGER.error("FavScore: " + vig.getAwayteamdata().getFinalscore());
									LOGGER.error("DogScore: " + vig.getHometeamdata().getFinalscore());
								}

								float finaldelta = vig.getHometeamdata().getFinalscore() - vig.getAwayteamdata().getFinalscore();
								if (finaldelta <= vig.getLine()) {
									if (showinfo) {
										LOGGER.error("Favorite WON");
									}
									wins += 1;
									currentdaywins += 1;
								} else {
									if (showinfo) {
										LOGGER.error("Favorite LOST");
									}
									losses += 1;
									currentdaylosses += 1;
								}
							} else if (vig.getHometeamdata().getShortname().equals(startspreadfavteam)) {
								if (showinfo) {
									LOGGER.error("Date: " + vig.getMonth() + "-" + vig.getDay() + "-" + vig.getYear());
									LOGGER.error("Line: " + vig.getLine());
									LOGGER.error("LineFavorite: " + vig.getLinefavorite());
									LOGGER.error("FavTeam: " + vig.getHometeamdata().getTeamname());
									LOGGER.error("DogTeam: " + vig.getAwayteamdata().getTeamname());
									LOGGER.error("FavScore: " + vig.getHometeamdata().getFinalscore());
									LOGGER.error("DogScore: " + vig.getAwayteamdata().getFinalscore());
								}

								float finaldelta = vig.getAwayteamdata().getFinalscore() - vig.getHometeamdata().getFinalscore();
								if (finaldelta <= vig.getLine()) {
									if (showinfo) {
										LOGGER.error("Favorite WON");
									}
									wins += 1;
									currentdaywins += 1;
								} else {
									if (showinfo) {
										LOGGER.error("Favorite LOST");
									}
									losses += 1;
									currentdaylosses += 1;
								}
							}
						} else if (delta <= -1) {
							// Means dog is trending to win
							if (vig.getAwayteamdata().getShortname().equals(startspreadfavteam)) {
								if (showinfo) {
									LOGGER.error("Date: " + vig.getMonth() + "-" + vig.getDay() + "-" + vig.getYear());
									LOGGER.error("Line: " + vig.getLine());
									LOGGER.error("LineFavorite: " + vig.getLinefavorite());
									LOGGER.error("FavTeam: " + vig.getAwayteamdata().getTeamname());
									LOGGER.error("DogTeam: " + vig.getHometeamdata().getTeamname());
									LOGGER.error("FavScore: " + vig.getAwayteamdata().getFinalscore());
									LOGGER.error("DogScore: " + vig.getHometeamdata().getFinalscore());
								}
								float finaldelta = vig.getHometeamdata().getFinalscore() - vig.getAwayteamdata().getFinalscore();
								if (finaldelta >= vig.getLine()) {
									if (showinfo) {
										LOGGER.error("Dog WON");
									}

									wins += 1;
									currentdaywins += 1;
								} else {
									if (showinfo) {
										LOGGER.error("Dog LOST");
									}
									losses += 1;
									currentdaylosses += 1;
								}
							} else if (vig.getHometeamdata().getShortname().equals(startspreadfavteam)) {
								if (showinfo) {
									LOGGER.error("Date: " + vig.getMonth() + "-" + vig.getDay() + "-" + vig.getYear());
									LOGGER.error("Line: " + vig.getLine());
									LOGGER.error("LineFavorite: " + vig.getLinefavorite());
									LOGGER.error("FavTeam: " + vig.getHometeamdata().getTeamname());
									LOGGER.error("DogTeam: " + vig.getAwayteamdata().getTeamname());
									LOGGER.error("FavScore: " + vig.getHometeamdata().getFinalscore());
									LOGGER.error("DogScore: " + vig.getAwayteamdata().getFinalscore());
								}
								float finaldelta = vig.getAwayteamdata().getFinalscore() - vig.getHometeamdata().getFinalscore();
								if (finaldelta >= vig.getLine()) {
									if (showinfo) {
										LOGGER.error("Dog WON");
									}
									wins += 1;
									currentdaywins += 1;
								} else {
									if (showinfo) {
										LOGGER.error("Dog LOST");
									}
									losses += 1;
									currentdaylosses += 1;
								}
							}
						}
					} else {
						// Same team, now check how far apart
						float ssf = Float.parseFloat(startspreadfav);
						float esf = Math.abs(Float.parseFloat(endspreadfav));
						float delta = esf - ssf;

						if (delta >= 1) {
							// Means fav is trending to win
							if (vig.getAwayteamdata().getShortname().equals(endspreadfavteam)) {
								if (showinfo) {
									LOGGER.error("Date: " + vig.getMonth() + "-" + vig.getDay() + "-" + vig.getYear());
									LOGGER.error("Line: " + vig.getLine());
									LOGGER.error("LineFavorite: " + vig.getLinefavorite());
									LOGGER.error("FavTeam: " + vig.getAwayteamdata().getTeamname());
									LOGGER.error("DogTeam: " + vig.getHometeamdata().getTeamname());
									LOGGER.error("FavScore: " + vig.getAwayteamdata().getFinalscore());
									LOGGER.error("DogScore: " + vig.getHometeamdata().getFinalscore());
								}
								float finaldelta = vig.getHometeamdata().getFinalscore() - vig.getAwayteamdata().getFinalscore();
								if (finaldelta <= vig.getLine()) {
									if (showinfo) {
										LOGGER.error("Favorite WON");
									}
									wins += 1;
									currentdaywins += 1;
								} else {
									if (showinfo) {
										LOGGER.error("Favorite LOST");
									}
									losses += 1;
									currentdaylosses += 1;
								}
							} else if (vig.getHometeamdata().getShortname().equals(endspreadfavteam)) {
								if (showinfo) {
									LOGGER.error("Date: " + vig.getMonth() + "-" + vig.getDay() + "-" + vig.getYear());
									LOGGER.error("Line: " + vig.getLine());
									LOGGER.error("LineFavorite: " + vig.getLinefavorite());
									LOGGER.error("FavTeam: " + vig.getHometeamdata().getTeamname());
									LOGGER.error("DogTeam: " + vig.getAwayteamdata().getTeamname());
									LOGGER.error("FavScore: " + vig.getHometeamdata().getFinalscore());
									LOGGER.error("DogScore: " + vig.getAwayteamdata().getFinalscore());
								}
								float finaldelta = vig.getAwayteamdata().getFinalscore() - vig.getHometeamdata().getFinalscore();
								if (finaldelta <= vig.getLine()) {
									if (showinfo) {
										LOGGER.error("Favorite WON");
									}
									wins += 1;
									currentdaywins += 1;
								} else {
									if (showinfo) {
										LOGGER.error("Favorite LOST");
									}
									losses += 1;
									currentdaylosses += 1;
								}
							}
						} else if (delta <= -1) {
							// Means dog is trending to win
							if (vig.getAwayteamdata().getShortname().equals(endspreadfavteam)) {
								if (showinfo) {
									LOGGER.error("Date: " + vig.getMonth() + "-" + vig.getDay() + "-" + vig.getYear());
									LOGGER.error("Line: " + vig.getLine());
									LOGGER.error("LineFavorite: " + vig.getLinefavorite());
									LOGGER.error("FavTeam: " + vig.getAwayteamdata().getTeamname());
									LOGGER.error("DogTeam: " + vig.getHometeamdata().getTeamname());
									LOGGER.error("FavScore: " + vig.getAwayteamdata().getFinalscore());
									LOGGER.error("DogScore: " + vig.getHometeamdata().getFinalscore());
								}
								float finaldelta = vig.getHometeamdata().getFinalscore() - vig.getAwayteamdata().getFinalscore();
								if (finaldelta > 0) {
									if (showinfo) {
										LOGGER.error("Dog WON");
									}
									wins += 1;
									currentdaywins += 1;
								} else if (finaldelta <= vig.getLine()) {
									if (showinfo) {
										LOGGER.error("Dog WON");
									}
									wins += 1;
									currentdaywins += 1;
								} else {
									if (showinfo) {
										LOGGER.error("Dog LOST");
									}
									losses += 1;
									currentdaylosses += 1;
								}
							} else if (vig.getHometeamdata().getShortname().equals(endspreadfavteam)) {
								if (showinfo) {
									LOGGER.error("Date: " + vig.getMonth() + "-" + vig.getDay() + "-" + vig.getYear());
									LOGGER.error("Line: " + vig.getLine());
									LOGGER.error("LineFavorite: " + vig.getLinefavorite());
									LOGGER.error("FavTeam: " + vig.getHometeamdata().getTeamname());
									LOGGER.error("DogTeam: " + vig.getAwayteamdata().getTeamname());
									LOGGER.error("FavScore: " + vig.getHometeamdata().getFinalscore());
									LOGGER.error("DogScore: " + vig.getAwayteamdata().getFinalscore());
								}
								float finaldelta = vig.getAwayteamdata().getFinalscore() - vig.getHometeamdata().getFinalscore();
								if (finaldelta > 0) {
									if (showinfo) {
										LOGGER.error("Dog WON");
									}
									wins += 1;
									currentdaywins += 1;
								} else if (finaldelta <= vig.getLine()) {
									if (showinfo) {
										LOGGER.error("Dog WON");
									}
									wins += 1;
									currentdaywins += 1;
								} else {
									if (showinfo) {
										LOGGER.error("Dog LOST");
									}
									losses += 1;
									currentdaylosses += 1;
								}
							}
						}
					}
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
					LOGGER.error("vigzzz: " + vig);
				}
			}

			LOGGER.error("Date: " + currentdate + " wins: " + currentdaywins + " losses: " + currentdaylosses);
			LOGGER.error("Total WINS: " + wins);
			LOGGER.error("Total LOSSES: " + losses);
		} catch (Throwable t) {
			LOGGER.debug(t.getMessage(), t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#loginToSite(java.lang.String, java.lang.String)
	 */
	@Override
	public String loginToSite(String username, String password) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String selectSport(String type) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected SiteTransaction createSiteTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}
}