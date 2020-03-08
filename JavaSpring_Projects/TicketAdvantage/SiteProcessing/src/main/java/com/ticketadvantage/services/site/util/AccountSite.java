/**
 * 
 */
package com.ticketadvantage.services.site.util;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.agsoftware.AGSoftwareProcessSite;
import com.ticketadvantage.services.dao.sites.betbuckeye.BetBuckeyeProcessSite;
import com.ticketadvantage.services.dao.sites.elitesports.EliteSportsProcessSite;
import com.ticketadvantage.services.dao.sites.fiveDimes.FiveDimesProcessSite;
import com.ticketadvantage.services.dao.sites.heritagesports.GsbettingProcessSite;
import com.ticketadvantage.services.dao.sites.heritagesports.HeritageSportsProcessSite;
import com.ticketadvantage.services.dao.sites.iol.IolSportsProcessSite;
import com.ticketadvantage.services.dao.sites.linetracker.LineTrackerProcessSite;
import com.ticketadvantage.services.dao.sites.linetracker.SkullBetProcessSite;
import com.ticketadvantage.services.dao.sites.metallica.MetallicaMobileProcessSite;
import com.ticketadvantage.services.dao.sites.metallica.MetallicaProcessSite;
import com.ticketadvantage.services.dao.sites.oddsmaker.OddsmakerProcessSite;
import com.ticketadvantage.services.dao.sites.pinnacle.PinnacleAPIProcessSite;
import com.ticketadvantage.services.dao.sites.play305.Play305MobileProcessSite;
import com.ticketadvantage.services.dao.sites.sports411.Sports411MobileBetslipProcessSite;
import com.ticketadvantage.services.dao.sites.sports411.Sports411MobileProcessSite;
import com.ticketadvantage.services.dao.sites.sports411.Sports411ProcessSite;
import com.ticketadvantage.services.dao.sites.stub.StubProcessSite;
import com.ticketadvantage.services.dao.sites.tangiers.TangiersProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.AbcWageringTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.Action23TDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.BetBigCityTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.BetOnUsaSportsTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.BetProPlusTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.BetallstarTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.BetgrandeTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.BetitTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.Ebets247TDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.FalconTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.FoursfoldTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.Green444TDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.IbetTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.LvActionTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.MyWagerLiveTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.PlayRSBTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.PlayTheDogTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.Playsports527TDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.SandIslandSportsTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.SevenTwentyFourTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsports.YoPigTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportseight.TDSportsEightProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportseleven.TDSportsElevenProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsfive.TDSportsFiveProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsfour.AbcwebTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsnine.TDSportsNineProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsone.TDSportsOneProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsseven.TDSportsSevenProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportssix.TDSportsSixProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsten.TDSportsTenProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportsthree.TDSportsThreeProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportstwelve.TDSportsTwelveProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportstwo.Bet504TDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportstwo.BetPantherTDSportsProcessSite;
import com.ticketadvantage.services.dao.sites.tdsportstwo.TDSportsTwoProcessSite;
import com.ticketadvantage.services.dao.sites.wagershack.BetGothamProcessSite;
import com.ticketadvantage.services.dao.sites.wagershack.WagerShackMobileProcessSite;
import com.ticketadvantage.services.dao.sites.wagerus.WagerusProcessSite;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.PreviewInput;

/**
 * @author jmiller
 *
 */
public class AccountSite {
	private static final Logger LOGGER = Logger.getLogger(AccountSite.class);

	/**
	 * 
	 */
	public AccountSite() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param account
	 * @return
	 * @throws BatchException
	 */
	public static SiteProcessor GetAccountSite(Accounts account) throws BatchException {
		LOGGER.info("Entering GetAccountSite()");
		SiteProcessor processSite = null;

		// Determine site type		
		if ("MetallicaMobile".equals(account.getSitetype())) {
			processSite = new MetallicaMobileProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Metallica".equals(account.getSitetype())) {
			processSite = new MetallicaProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("BetBuckeye".equals(account.getSitetype())) {
			processSite = new BetBuckeyeProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("HeritageSports".equals(account.getSitetype())) {
			processSite = new HeritageSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("SkullBet".equals(account.getSitetype())) {
			processSite = new SkullBetProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("LineTracker".equals(account.getSitetype())) {
			processSite = new LineTrackerProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("AGSoftware".equals(account.getSitetype())) {
			processSite = new AGSoftwareProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("EliteSports".equals(account.getSitetype())) {
			processSite = new EliteSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("WagerShackMobile".equals(account.getSitetype())) {
			processSite = new WagerShackMobileProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("BetGotham".equals(account.getSitetype())) {
			processSite = new BetGothamProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Sports411Mobile".equals(account.getSitetype())) {
			processSite = new Sports411MobileProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Sports411".equals(account.getSitetype())) {
			processSite = new Sports411ProcessSite(account.getUrl(), account.getUsername(), account.getPassword());
		} else if ("Green444TDSports".equals(account.getSitetype())) {
			processSite = new Green444TDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("FoursfoldTDSportsProcessSite".equals(account.getSitetype())) {
			processSite = new FoursfoldTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("AbcwebTDSports".equals(account.getSitetype())) {
			processSite = new AbcwebTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("SevenTwentyFourTDSports".equals(account.getSitetype())) {
			processSite = new SevenTwentyFourTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("BetgrandeTDSports".equals(account.getSitetype())) {
			processSite = new BetgrandeTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("IolSports".equals(account.getSitetype())) {
			processSite = new IolSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("LvActionTDSports".equals(account.getSitetype())) {
			processSite = new LvActionTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("IbetTDSports".equals(account.getSitetype())) {
			processSite = new IbetTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("PlayTheDogTDSports".equals(account.getSitetype())) {
			processSite = new PlayTheDogTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("YoPigTDSports".equals(account.getSitetype())) {
			processSite = new YoPigTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("PlayRSBTDSports".equals(account.getSitetype())) {
			processSite = new PlayRSBTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("AbcWageringTDSports".equals(account.getSitetype())) {
			processSite = new AbcWageringTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Ebets247TDSports".equals(account.getSitetype())) {
			processSite = new Ebets247TDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Playsports527TDSports".equals(account.getSitetype())) {
			processSite = new Playsports527TDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsOne".equals(account.getSitetype())) {
			processSite = new TDSportsOneProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("SandIslandSportsTDSports".equals(account.getSitetype())) {
			processSite = new SandIslandSportsTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("BetitTDSports".equals(account.getSitetype())) {
			processSite = new BetitTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("BetProPlusTDSports".equals(account.getSitetype())) {
			processSite = new BetProPlusTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSports".equals(account.getSitetype())) {
			processSite = new TDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsFive".equals(account.getSitetype())) {
			processSite = new TDSportsFiveProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsSix".equals(account.getSitetype())) {
			processSite = new TDSportsSixProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Gsbetting".equals(account.getSitetype())) {
			processSite = new GsbettingProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("BetOnUsaSportsTDSports".equals(account.getSitetype())) {
			processSite = new BetOnUsaSportsTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("BetPantherTDSports".equals(account.getSitetype())) {
			processSite = new BetPantherTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Action23TDSports".equals(account.getSitetype())) {
			processSite = new Action23TDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsThree".equals(account.getSitetype())) {
			processSite = new TDSportsThreeProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsNine".equals(account.getSitetype())) {
			processSite = new TDSportsNineProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsEleven".equals(account.getSitetype())) {
			processSite = new TDSportsElevenProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsTwo".equals(account.getSitetype())) {
			processSite = new TDSportsTwoProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsTen".equals(account.getSitetype())) {
			processSite = new TDSportsTenProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Bet504TDSports".equals(account.getSitetype())) {
			processSite = new Bet504TDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("BetallstarTDSports".equals(account.getSitetype())) {
			processSite = new BetallstarTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("MyWagerLiveTDSports".equals(account.getSitetype())) {
			processSite = new MyWagerLiveTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("BetBigCityTDSports".equals(account.getSitetype())) {
			processSite = new BetBigCityTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsNew".equals(account.getSitetype())) {
			// processSite = new TDSportsNewProcessSite(account.getUrl(),
			// account.getUsername(), account.getPassword());
		} else if ("FiveDimes".equals(account.getSitetype())) {
			processSite = new FiveDimesProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Wagerus".equals(account.getSitetype())) {
			processSite = new WagerusProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Tangiers".equals(account.getSitetype())) {
			processSite = new TangiersProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Oddsmaker".equals(account.getSitetype())) {
			processSite = new OddsmakerProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Pinnacle".equals(account.getSitetype())) {
			processSite = new PinnacleAPIProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Play305".equals(account.getSitetype())) {
			processSite = new Play305MobileProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("FalconTDSports".equals(account.getSitetype())) {
			processSite = new FalconTDSportsProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("Stub".equals(account.getSitetype())) {
			processSite = new StubProcessSite(account.getUrl(), account.getUsername(), account.getPassword());
		} else if ("Sports411MobileBetslip".equals(account.getSitetype())) {
			processSite = new Sports411MobileBetslipProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsTwelve".equals(account.getSitetype())) {
			processSite = new TDSportsTwelveProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsSeven".equals(account.getSitetype())) {
			processSite = new TDSportsSevenProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else if ("TDSportsEight".equals(account.getSitetype())) {
			processSite = new TDSportsEightProcessSite(account.getUrl(), account.getUsername(), account.getPassword(), account.getIsmobile(), account.getShowrequestresponse());
		} else {
			throw new BatchException(BatchErrorCodes.SITE_PROVIDER_NOT_FOUND, BatchErrorMessage.SITE_PROVIDER_NOT_FOUND,
					"Site type " + account.getSitetype() + " is not available");
		}

		LOGGER.info("Exiting GetAccountSite()");
		return processSite;
	}

	/**
	 * 
	 * @param previewInput
	 * @return
	 * @throws BatchException
	 */
	public static SiteProcessor GetAccountSite(PreviewInput previewInput) throws BatchException {
		LOGGER.info("Entering GetAccountSite()");
		SiteProcessor processSite = null;

		// Determine site type		
		if ("MetallicaMobile".equals(previewInput.getSitetype())) {
			processSite = new MetallicaMobileProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Metallica".equals(previewInput.getSitetype())) {
			processSite = new MetallicaProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("BetBuckeye".equals(previewInput.getSitetype())) {
			processSite = new BetBuckeyeProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("HeritageSports".equals(previewInput.getSitetype())) {
			processSite = new HeritageSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("SkullBet".equals(previewInput.getSitetype())) {
			processSite = new SkullBetProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("LineTracker".equals(previewInput.getSitetype())) {
			processSite = new LineTrackerProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("AGSoftware".equals(previewInput.getSitetype())) {
			processSite = new AGSoftwareProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("EliteSports".equals(previewInput.getSitetype())) {
			processSite = new EliteSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("WagerShackMobile".equals(previewInput.getSitetype())) {
			processSite = new WagerShackMobileProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("BetGotham".equals(previewInput.getSitetype())) {
			processSite = new BetGothamProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Sports411Mobile".equals(previewInput.getSitetype())) {
			processSite = new Sports411MobileProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Sports411".equals(previewInput.getSitetype())) {
			processSite = new Sports411ProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword());
		} else if ("Green444TDSports".equals(previewInput.getSitetype())) {
			processSite = new Green444TDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("FoursfoldTDSportsProcessSite".equals(previewInput.getSitetype())) {
			processSite = new FoursfoldTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("AbcwebTDSports".equals(previewInput.getSitetype())) {
			processSite = new AbcwebTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("SevenTwentyFourTDSports".equals(previewInput.getSitetype())) {
			processSite = new SevenTwentyFourTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("BetgrandeTDSports".equals(previewInput.getSitetype())) {
			processSite = new BetgrandeTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("IolSports".equals(previewInput.getSitetype())) {
			processSite = new IolSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("LvActionTDSports".equals(previewInput.getSitetype())) {
			processSite = new LvActionTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("IbetTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("IbetTDSports selected");
			processSite = new IbetTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("PlayTheDogTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("PlayTheDogTDSports selected");
			processSite = new PlayTheDogTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("YoPigTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("YoPigTDSports selected");
			processSite = new YoPigTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("PlayRSBTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("PlayRSBTDSports selected");
			processSite = new PlayRSBTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("AbcWageringTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("AbcWageringTDSports selected");
			processSite = new AbcWageringTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Ebets247TDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("Ebets247TDSports selected");
			processSite = new Ebets247TDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Playsports527TDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("Playsports527TDSports selected");
			processSite = new Playsports527TDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsOne".equals(previewInput.getSitetype())) {
			LOGGER.error("TDSportsOne selected");
			processSite = new TDSportsOneProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("SandIslandSportsTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("SandIslandSportsTDSports selected");
			processSite = new SandIslandSportsTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("BetitTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("BetitTDSports selected");
			processSite = new BetitTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("BetProPlusTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("BetProPlusTDSports selected");
			processSite = new BetProPlusTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("TDSports selected");
			processSite = new TDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsFive".equals(previewInput.getSitetype())) {
			LOGGER.error("TDSportsFive selected");
			processSite = new TDSportsFiveProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsSix".equals(previewInput.getSitetype())) {
			LOGGER.error("TDSportsSix selected");
			processSite = new TDSportsSixProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Gsbetting".equals(previewInput.getSitetype())) {
			LOGGER.error("Gsbetting selected");
			processSite = new GsbettingProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("BetOnUsaSportsTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("BetOnUsaSportsTDSports selected");
			processSite = new BetOnUsaSportsTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Action23TDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("Action23TDSports selected");
			processSite = new Action23TDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsThree".equals(previewInput.getSitetype())) {
			LOGGER.error("TDSportsThree selected");
			processSite = new TDSportsThreeProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsNine".equals(previewInput.getSitetype())) {
			LOGGER.error("TDSportsNine selected");
			processSite = new TDSportsNineProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsEleven".equals(previewInput.getSitetype())) {
			LOGGER.error("TDSportsEleven selected");
			processSite = new TDSportsElevenProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsTwo".equals(previewInput.getSitetype())) {
			LOGGER.error("TDSportsTwo selected");
			processSite = new TDSportsTwoProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsTen".equals(previewInput.getSitetype())) {
			LOGGER.error("TDSportsTen selected");
			processSite = new TDSportsTenProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Bet504TDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("Bet504TDSports selected");
			processSite = new Bet504TDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("BetallstarTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("BetallstarTDSports selected");
			processSite = new BetallstarTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("BetPantherTDSports".equals(previewInput.getSitetype())) {
			LOGGER.error("BetPantherTDSports selected");
			processSite = new BetPantherTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Pinnacle".equals(previewInput.getSitetype())) {
			LOGGER.error("Pinnacle selected");
			processSite = new PinnacleAPIProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("FiveDimes".equals(previewInput.getSitetype())) {
			LOGGER.error("FiveDimes selected");
			processSite = new FiveDimesProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Wagerus".equals(previewInput.getSitetype())) {
			LOGGER.error("Wagerus selected");
			processSite = new WagerusProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Tangiers".equals(previewInput.getSitetype())) {
			LOGGER.error("Tangiers selected");
			processSite = new TangiersProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Oddsmaker".equals(previewInput.getSitetype())) {
			LOGGER.error("Oddsmaker selected");
			processSite = new OddsmakerProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("BetBigCityTDSports".equals(previewInput.getSitetype())) {
			processSite = new BetBigCityTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Play305".equals(previewInput.getSitetype())) {
			processSite = new Play305MobileProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("FalconTDSports".equals(previewInput.getSitetype())) {
			processSite = new FalconTDSportsProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("Sports411MobileBetslip".equals(previewInput.getSitetype())) {
			processSite = new Sports411MobileBetslipProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsTwelve".equals(previewInput.getSitetype())) {
			processSite = new TDSportsTwelveProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());			
		} else if ("TDSportsSeven".equals(previewInput.getSitetype())) {
			processSite = new TDSportsSevenProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsEight".equals(previewInput.getSitetype())) {
			processSite = new TDSportsEightProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword(), previewInput.getIsmobile(), previewInput.getShowrequestresponse());
		} else if ("TDSportsNew".equals(previewInput.getSitetype())) {
			// processSite = new TDSportsNewProcessSite(previewInput.getUrl(),
			// previewInput.getUsername(), previewInput.getPassword());
		} else if ("Stub".equals(previewInput.getSitetype())) {
			processSite = new StubProcessSite(previewInput.getUrl(), previewInput.getUsername(), previewInput.getPassword());
		} else {
			throw new BatchException(BatchErrorCodes.SITE_PROVIDER_NOT_FOUND, BatchErrorMessage.SITE_PROVIDER_NOT_FOUND,
					"Site type " + previewInput.getSitetype() + " is not available");
		}

		LOGGER.info("Exiting GetAccountSite()");
		return processSite;
	}

	/**
	 * 
	 * @param sportType
	 * @return
	 */
	public static String GetSportName(String sportType) {
		LOGGER.info("Entering GetSportName()");
		String sportName = "";

		if (sportType.contains("nfl")) {
			sportName = "NFL";
		} else if (sportType.contains("ncaaf")) {
			sportName = "NCAAF";
		} else if (sportType.contains("wnba")) {
			sportName = "WNBA";
		} else if (sportType.contains("nba")) {
			sportName = "NBA";
		} else if (sportType.contains("ncaab")) {
			sportName = "NCAAB";
		} else if (sportType.contains("nhl")) {
			sportName = "NHL";
		} else if (sportType.contains("mlb")) {
			sportName = "MLB";
		}

		LOGGER.info("Exiting GetSportName()");
		return sportName;
	}
}