/**
 * 
 */
package com.ticketadvantage.services.dao.sites.wagershack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SelectOptionData;
import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class BetGothamParser extends WagerShackMobileParser {
	private static final Logger LOGGER = Logger.getLogger(BetGothamParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm:ss a z");

	/**
	 * Constructor
	 */
	public BetGothamParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final BetGothamParser betGothamParser = new BetGothamParser();
			betGothamParser.setTimezone("ET");

			final String xhtml = "<div class='BT-header'> BET(S) ACCEPTED</div><div class='BT-allcont'><div class='row' style='margin:0px;'><div class='col-lg-12 BT-total-header' style='border:none;'>STRAIGHT BET<br><strong>Ticket Number : 3432147</strong><br />[980] TOTAL u8&frac12;-110 (SAN DIEGO PADRES vrs HOUSTON ASTROS)<br>(B. MITCHELL - R / G. COLE - R)<br>Risk: 55.00 Win: 50.00<hr />					<div class=\"btn-group btn-group-justified\" role=\"group\">					  <div class=\"btn-group\" role=\"group\">						<a herf=\"javascript:;\" class=\"btn btn-success\" id=\"mainBtn\" onClick=\"startBet()\">New Bet</a>					  </div>					  <div class=\"btn-group\" role=\"group\">						<a href=\"OpenBets.aspx\" class=\"btn btn-primary\">Pending Bets</a>					  </div>					</div>            								</div></div></div>";
			String ticketNumber = betGothamParser.parseTicketNumber(xhtml);
			LOGGER.debug("ticketNumber: " + ticketNumber);

//			final String xhtml = "<div class='panel'><div class='panel-title linesPanelTitle'>MLB GAME LINES<ul class='panel-tools' style='top: 6px;'><li><a style='color:#fff' class='icon minimise-tool'><i class='fa fa-minus'></i></a></li></ul></div><div class='panel-body'><div class='row'><div class='linesHeader row-offset-0 hidden-xs col-xs-1'>TIME</div><div class='linesHeader row-offset-0 hidden-xs col-xs-1'>ROT</div><div class='linesHeader row-offset-0 hidden-xs col-lg-4 col-md-4  col-sm-4 col-xs-3'>TEAM</div><div class='linesHeader row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>SPREAD</div><div class='linesHeader row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>TOTAL</div><div class='linesHeader row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>ML</div></div><div class='row gameDate'><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>MLB GAME LINES  - Apr 07</div><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'></div></div><div class='row gameDate'><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>NATIONAL LEAGUE</div><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>SATURDAY, APRIL 7TH, 2018</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:20:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>953</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>953 </span> <span class='mlb-diamondbacks float-l teamnsLogos'></span> ARIZONA DIAMONDBACKS - Z. GREINKE - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:20 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759215','S','ARIZONA DIAMONDBACKS','1.5','-235','MLB','70','953');\"><a href='javascript:;' id='759215_0_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -235</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759215','Ov','ARIZONA DIAMONDBACKS','7.5','105','MLB','70','953');\"><a href='javascript:;' id='759215_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 7&frac12;+105</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759215','ML','ARIZONA DIAMONDBACKS','0','-110','MLB','70','954');\"><a href='javascript:;' id='759215_4_ML' class='btn btn-light btn-sm btn-block regular-line'>-110</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>954</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>954 </span> <span class='mlb-cardinals float-l teamnsLogos'></span> ST. LOUIS CARDINALS - M. WACHA - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759215','S','ST. LOUIS CARDINALS','-1.5','195','MLB','70','954');\"><a href='javascript:;' id='759215_1_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; +195</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759215','Un','ST. LOUIS CARDINALS','7.5','-125','MLB','70','954');\"><a href='javascript:;' id='759215_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 7&frac12;-125</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759215','ML','ST. LOUIS CARDINALS','0','-100','MLB','70','954');\"><a href='javascript:;' id='759215_5_ML' class='btn btn-light btn-sm btn-block regular-line'>EV</a></div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>4:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>955</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>955 </span> <span class='mlb-angeles-dodgers float-l teamnsLogos'></span> LOS ANGELES DODGERS - R. HILL - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 4:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759457','S','LOS ANGELES DODGERS','-1.5','130','MLB','70','955');\"><a href='javascript:;' id='759457_0_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; +130</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759457','Ov','LOS ANGELES DODGERS','7.5','-110','MLB','70','955');\"><a href='javascript:;' id='759457_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 7&frac12;-110</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759457','ML','LOS ANGELES DODGERS','0','-130','MLB','70','956');\"><a href='javascript:;' id='759457_4_ML' class='btn btn-light btn-sm btn-block regular-line'>-130</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>956</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>956 </span> <span class='mlb-giants float-l teamnsLogos'></span> SAN FRANCISCO GIANTS - C. STRATTON - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759457','S','SAN FRANCISCO GIANTS','1.5','-150','MLB','70','956');\"><a href='javascript:;' id='759457_1_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -150</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759457','Un','SAN FRANCISCO GIANTS','7.5','-110','MLB','70','956');\"><a href='javascript:;' id='759457_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 7&frac12;-110</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759457','ML','SAN FRANCISCO GIANTS','0','110','MLB','70','956');\"><a href='javascript:;' id='759457_5_ML' class='btn btn-light btn-sm btn-block regular-line'>+110</a></div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>4:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>957</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>957 </span> <span class='mlb-cubs float-l teamnsLogos'></span> CHICAGO CUBS - Y. DARVISH - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 4:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759221','S','CHICAGO CUBS','-1.5','115','MLB','70','957');\"><a href='javascript:;' id='759221_0_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; +115</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759221','Ov','CHICAGO CUBS','8.5','-115','MLB','70','957');\"><a href='javascript:;' id='759221_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 8&frac12;-115</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759221','ML','CHICAGO CUBS','0','-140','MLB','70','958');\"><a href='javascript:;' id='759221_4_ML' class='btn btn-light btn-sm btn-block regular-line'>-140</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>958</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>958 </span> <span class='mlb-brewers float-l teamnsLogos'></span> MILWAUKEE BREWERS - Z. DAVIES - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759221','S','MILWAUKEE BREWERS','1.5','-135','MLB','70','958');\"><a href='javascript:;' id='759221_1_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -135</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759221','Un','MILWAUKEE BREWERS','8.5','-105','MLB','70','958');\"><a href='javascript:;' id='759221_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 8&frac12;-105</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759221','ML','MILWAUKEE BREWERS','0','120','MLB','70','958');\"><a href='javascript:;' id='759221_5_ML' class='btn btn-light btn-sm btn-block regular-line'>+120</a></div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>6:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>959</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>959 </span> <span class='mlb-marlins float-l teamnsLogos'></span> MIAMI MARLINS - D. PETERS - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 6:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759224','S','MIAMI MARLINS','1.5','-160','MLB','70','959');\"><a href='javascript:;' id='759224_0_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -160</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759224','Ov','MIAMI MARLINS','8','-110','MLB','70','959');\"><a href='javascript:;' id='759224_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 8-110</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759224','ML','MIAMI MARLINS','0','130','MLB','70','960');\"><a href='javascript:;' id='759224_4_ML' class='btn btn-light btn-sm btn-block regular-line'>+130</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>960</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>960 </span> <span class='mlb-phillies float-l teamnsLogos'></span> PHILADELPHIA PHILLIES - V. VELASQUEZ - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759224','S','PHILADELPHIA PHILLIES','-1.5','140','MLB','70','960');\"><a href='javascript:;' id='759224_1_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; +140</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759224','Un','PHILADELPHIA PHILLIES','8','-110','MLB','70','960');\"><a href='javascript:;' id='759224_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 8-110</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759224','ML','PHILADELPHIA PHILLIES','0','-150','MLB','70','960');\"><a href='javascript:;' id='759224_5_ML' class='btn btn-light btn-sm btn-block regular-line'>-150</a></div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>7:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>961</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>961 </span> <span class='mlb-reds float-l teamnsLogos'></span> CINCINNATI REDS - S. ROMANO - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 7:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759227','S','CINCINNATI REDS','1.5','-190','MLB','70','961');\"><a href='javascript:;' id='759227_0_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -190</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759227','Ov','CINCINNATI REDS','8','-115','MLB','70','961');\"><a href='javascript:;' id='759227_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 8-115</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759227','ML','CINCINNATI REDS','0','110','MLB','70','962');\"><a href='javascript:;' id='759227_4_ML' class='btn btn-light btn-sm btn-block regular-line'>+110</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>962</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>962 </span> <span class='mlb-pirates float-l teamnsLogos'></span> PITTSBURGH PIRATES - C. KUHL - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759227','S','PITTSBURGH PIRATES','-1.5','160','MLB','70','962');\"><a href='javascript:;' id='759227_1_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; +160</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759227','Un','PITTSBURGH PIRATES','8','-105','MLB','70','962');\"><a href='javascript:;' id='759227_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 8-105</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759227','ML','PITTSBURGH PIRATES','0','-130','MLB','70','962');\"><a href='javascript:;' id='759227_5_ML' class='btn btn-light btn-sm btn-block regular-line'>-130</a></div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>8:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>963</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>963 </span> <span class='mlb-braves float-l teamnsLogos'></span> ATLANTA BRAVES - AN. SANCHEZ - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 8:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759338','S','ATLANTA BRAVES','1.5','-155','MLB','70','963');\"><a href='javascript:;' id='759338_0_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -155</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759338','Ov','ATLANTA BRAVES','11','-105','MLB','70','963');\"><a href='javascript:;' id='759338_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 11-105</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759338','ML','ATLANTA BRAVES','0','120','MLB','70','964');\"><a href='javascript:;' id='759338_4_ML' class='btn btn-light btn-sm btn-block regular-line'>+120</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>964</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>964 </span> <span class='mlb-rockies float-l teamnsLogos'></span> COLORADO ROCKIES - C. BETTIS - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759338','S','COLORADO ROCKIES','-1.5','135','MLB','70','964');\"><a href='javascript:;' id='759338_1_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; +135</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759338','Un','COLORADO ROCKIES','11','-115','MLB','70','964');\"><a href='javascript:;' id='759338_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 11-115</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759338','ML','COLORADO ROCKIES','0','-140','MLB','70','964');\"><a href='javascript:;' id='759338_5_ML' class='btn btn-light btn-sm btn-block regular-line'>-140</a></div></div><div class='lnSeparator'></div><div class='row gameDate'><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>AMERICAN LEAGUE</div><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>SATURDAY, APRIL 7TH, 2018</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>969</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>969 </span> <span class='mlb-tigers float-l teamnsLogos'></span> DETROIT TIGERS - M. FULMER - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759240','S','DETROIT TIGERS','1.5','-205','MLB','70','969');\"><a href='javascript:;' id='759240_0_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -205</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759240','Ov','DETROIT TIGERS','8','-110','MLB','70','969');\"><a href='javascript:;' id='759240_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 8-110</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759240','ML','DETROIT TIGERS','0','100','MLB','70','970');\"><a href='javascript:;' id='759240_4_ML' class='btn btn-light btn-sm btn-block regular-line'>EV</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>970</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>970 </span> <span class='mlb-sox float-l teamnsLogos'></span> CHICAGO WHITE SOX - L. GIOLITO - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759240','S','CHICAGO WHITE SOX','-1.5','170','MLB','70','970');\"><a href='javascript:;' id='759240_1_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; +170</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759240','Un','CHICAGO WHITE SOX','8','-110','MLB','70','970');\"><a href='javascript:;' id='759240_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 8-110</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759240','ML','CHICAGO WHITE SOX','0','-120','MLB','70','970');\"><a href='javascript:;' id='759240_5_ML' class='btn btn-light btn-sm btn-block regular-line'>-120</a></div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>971</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>971 </span> <span class='mlb-mariners float-l teamnsLogos'></span> SEATTLE MARINERS - M. LEAKE - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759243','S','SEATTLE MARINERS','1.5','-145','MLB','70','971');\"><a href='javascript:;' id='759243_0_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -145</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759243','Ov','SEATTLE MARINERS','8','-110','MLB','70','971');\"><a href='javascript:;' id='759243_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 8-110</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759243','ML','SEATTLE MARINERS','0','140','MLB','70','972');\"><a href='javascript:;' id='759243_4_ML' class='btn btn-light btn-sm btn-block regular-line'>+140</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>972</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>972 </span> <span class='mlb-twins float-l teamnsLogos'></span> MINNESOTA TWINS - J. BERRIOS - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759243','S','MINNESOTA TWINS','-1.5','125','MLB','70','972');\"><a href='javascript:;' id='759243_1_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; +125</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759243','Un','MINNESOTA TWINS','8','-110','MLB','70','972');\"><a href='javascript:;' id='759243_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 8-110</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759243','ML','MINNESOTA TWINS','0','-160','MLB','70','972');\"><a href='javascript:;' id='759243_5_ML' class='btn btn-light btn-sm btn-block regular-line'>-160</a></div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>4:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>973</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>973 </span> <span class='mlb-city-royals float-l teamnsLogos'></span> KANSAS CITY ROYALS - I. KENNEDY - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 4:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759246','S','KANSAS CITY ROYALS','1.5','-110','MLB','70','973');\"><a href='javascript:;' id='759246_0_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -110</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759246','Ov','KANSAS CITY ROYALS','8','-110','MLB','70','973');\"><a href='javascript:;' id='759246_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 8-110</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759246','ML','KANSAS CITY ROYALS','0','180','MLB','70','974');\"><a href='javascript:;' id='759246_4_ML' class='btn btn-light btn-sm btn-block regular-line'>+180</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>974</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>974 </span> <span class='mlb-indians float-l teamnsLogos'></span> CLEVELAND INDIANS - T. BAUER - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759246','S','CLEVELAND INDIANS','-1.5','-110','MLB','70','974');\"><a href='javascript:;' id='759246_1_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; -110</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759246','Un','CLEVELAND INDIANS','8','-110','MLB','70','974');\"><a href='javascript:;' id='759246_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 8-110</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759246','ML','CLEVELAND INDIANS','0','-220','MLB','70','974');\"><a href='javascript:;' id='759246_5_ML' class='btn btn-light btn-sm btn-block regular-line'>-220</a></div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>8:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>975</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>975 </span> <span class='mlb-blue-jays float-l teamnsLogos'></span> TORONTO BLUE JAYS - M. STROMAN - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 8:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759249','S','TORONTO BLUE JAYS','-1.5','135','MLB','70','975');\"><a href='javascript:;' id='759249_0_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; +135</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759249','Ov','TORONTO BLUE JAYS','8.5','-105','MLB','70','975');\"><a href='javascript:;' id='759249_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 8&frac12;-105</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759249','ML','TORONTO BLUE JAYS','0','-115','MLB','70','976');\"><a href='javascript:;' id='759249_4_ML' class='btn btn-light btn-sm btn-block regular-line'>-115</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>976</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>976 </span> <span class='mlb-rangers float-l teamnsLogos'></span> TEXAS RANGERS - M. MINOR - L </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759249','S','TEXAS RANGERS','1.5','-155','MLB','70','976');\"><a href='javascript:;' id='759249_1_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -155</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759249','Un','TEXAS RANGERS','8.5','-115','MLB','70','976');\"><a href='javascript:;' id='759249_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 8&frac12;-115</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759249','ML','TEXAS RANGERS','0','-105','MLB','70','976');\"><a href='javascript:;' id='759249_5_ML' class='btn btn-light btn-sm btn-block regular-line'>-105</a></div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>9:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>977</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>977 </span> <span class='mlb-athletics float-l teamnsLogos'></span> OAKLAND ATHLETICS - A. TRIGGS - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 9:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759252','S','OAKLAND ATHLETICS','-1.5','145','MLB','70','977');\"><a href='javascript:;' id='759252_0_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; +145</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759252','Ov','OAKLAND ATHLETICS','8.5','-115','MLB','70','977');\"><a href='javascript:;' id='759252_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 8&frac12;-115</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759252','ML','OAKLAND ATHLETICS','0','-100','MLB','70','978');\"><a href='javascript:;' id='759252_4_ML' class='btn btn-light btn-sm btn-block regular-line'>EV</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>978</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>978 </span> <span class='mlb-angels float-l teamnsLogos'></span> LOS ANGELES ANGELS - JC RAMIREZ - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759252','S','LOS ANGELES ANGELS','1.5','-165','MLB','70','978');\"><a href='javascript:;' id='759252_1_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; -165</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759252','Un','LOS ANGELES ANGELS','8.5','-105','MLB','70','978');\"><a href='javascript:;' id='759252_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 8&frac12;-105</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759252','ML','LOS ANGELES ANGELS','0','-110','MLB','70','978');\"><a href='javascript:;' id='759252_5_ML' class='btn btn-light btn-sm btn-block regular-line'>-110</a></div></div><div class='lnSeparator'></div><div class='row gameDate'><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>INTERLEAGUE</div><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>SATURDAY, APRIL 7TH, 2018</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>7:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>979</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>979 </span> <span class='mlb-diego-padres float-l teamnsLogos'></span> SAN DIEGO PADRES - B. MITCHELL - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 7:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('0','759256','S','SAN DIEGO PADRES','1.5','120','MLB','70','979');\"><a href='javascript:;' id='759256_0_S' class='btn btn-light btn-sm btn-block regular-line'>+1&frac12; +120</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('2','759256','Ov','SAN DIEGO PADRES','8','-120','MLB','70','979');\"><a href='javascript:;' id='759256_2_Ov' class='btn btn-light btn-sm btn-block regular-line'>Ov 8-120</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('4','759256','ML','SAN DIEGO PADRES','0','220','MLB','70','980');\"><a href='javascript:;' id='759256_4_ML' class='btn btn-light btn-sm btn-block regular-line'>+220</a></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>980</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>980 </span> <span class='mlb-astros float-l teamnsLogos'></span> HOUSTON ASTROS - G. COLE - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'  onclick=\"addbet('1','759256','S','HOUSTON ASTROS','-1.5','-140','MLB','70','980');\"><a href='javascript:;' id='759256_1_S' class='btn btn-light btn-sm btn-block regular-line'>-1&frac12; -140</a></div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('3','759256','Un','HOUSTON ASTROS','8','100','MLB','70','980');\"><a href='javascript:;' id='759256_3_Un' class='btn btn-light btn-sm btn-block regular-line'>Un 8EV</a></div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4' onclick=\"addbet('5','759256','ML','HOUSTON ASTROS','0','-280','MLB','70','980');\"><a href='javascript:;' id='759256_5_ML' class='btn btn-light btn-sm btn-block regular-line'>-280</a></div></div><div class='lnSeparator'></div><div class='row gameDate'><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>MLB GAME LINES  - Apr 08</div><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'></div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:40:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>901</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>901 </span> <span class='mlb-reds float-l teamnsLogos'></span> CINCINNATI REDS - T. MAHLE - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:40 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>902</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>902 </span> <span class='mlb-pirates float-l teamnsLogos'></span> PITTSBURGH PIRATES - J. TAILLON - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:40:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>903</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>903 </span> <span class='mlb-marlins float-l teamnsLogos'></span> MIAMI MARLINS - T. RICHARDS - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:40 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>904</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>904 </span> <span class='mlb-phillies float-l teamnsLogos'></span> PHILADELPHIA PHILLIES - J. ARRIETA - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>905</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>905 </span> <span class='mlb-cubs float-l teamnsLogos'></span> CHICAGO CUBS - J. QUINTANA - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>906</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>906 </span> <span class='mlb-brewers float-l teamnsLogos'></span> MILWAUKEE BREWERS - CH. ANDERSON - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:20:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>907</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>907 </span> <span class='mlb-diamondbacks float-l teamnsLogos'></span> ARIZONA DIAMONDBACKS - T. WALKER - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:20 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>908</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>908 </span> <span class='mlb-cardinals float-l teamnsLogos'></span> ST. LOUIS CARDINALS - L. WEAVER - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>4:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>911</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>911 </span> <span class='mlb-angeles-dodgers float-l teamnsLogos'></span> LOS ANGELES DODGERS - C. KERSHAW - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 4:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>912</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>912 </span> <span class='mlb-giants float-l teamnsLogos'></span> SAN FRANCISCO GIANTS - T. BLACH - L </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row gameDate'><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>NATIONAL LEAGUE</div><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>SUNDAY, APRIL 8TH, 2018</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>8:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>913</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>913 </span> <span class='mlb-york-mets float-l teamnsLogos'></span> NEW YORK METS - M. HARVEY - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 8:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>914</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>914 </span> <span class='mlb-nationals float-l teamnsLogos'></span> WASHINGTON NATIONALS - T. ROARK - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>915</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>915 </span> <span class='mlb-orioles float-l teamnsLogos'></span> BALTIMORE ORIOLES - M. WRIGHT JR. - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>916</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>916 </span> <span class='mlb-yankees float-l teamnsLogos'></span> NEW YORK YANKEES - J. MONTGOMERY - L </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>919</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>919 </span> <span class='mlb-city-royals float-l teamnsLogos'></span> KANSAS CITY ROYALS - J. HAMMEL - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>920</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>920 </span> <span class='mlb-indians float-l teamnsLogos'></span> CLEVELAND INDIANS - M. CLEVINGER - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>921</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>921 </span> <span class='mlb-tigers float-l teamnsLogos'></span> DETROIT TIGERS - M. FIERS - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>922</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>922 </span> <span class='mlb-sox float-l teamnsLogos'></span> CHICAGO WHITE SOX - R. LOPEZ - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>923</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>923 </span> <span class='mlb-mariners float-l teamnsLogos'></span> SEATTLE MARINERS - M. GONZALES - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>924</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>924 </span> <span class='mlb-twins float-l teamnsLogos'></span> MINNESOTA TWINS - L. LYNN - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>3:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>925</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>925 </span> <span class='mlb-blue-jays float-l teamnsLogos'></span> TORONTO BLUE JAYS - J. GARCIA - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 3:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>926</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>926 </span> <span class='mlb-rangers float-l teamnsLogos'></span> TEXAS RANGERS - C. HAMELS - L </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row gameDate'><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>AMERICAN LEAGUE</div><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>SUNDAY, APRIL 8TH, 2018</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>4:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>927</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>927 </span> <span class='mlb-athletics float-l teamnsLogos'></span> OAKLAND ATHLETICS - K. GRAVEMAN - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 4:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>928</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>928 </span> <span class='mlb-angels float-l teamnsLogos'></span> LOS ANGELES ANGELS - S. OHTANI - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row gameDate'><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>INTERLEAGUE</div><div class='col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left'>SUNDAY, APRIL 8TH, 2018</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>929</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>929 </span> <span class='mlb-diego-padres float-l teamnsLogos'></span> SAN DIEGO PADRES - T. ROSS - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>930</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>930 </span> <span class='mlb-astros float-l teamnsLogos'></span> HOUSTON ASTROS - C. MORTON - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:40:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809011</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809011 </span> <span class='mlb-cincinnati-reds float-l teamnsLogos'></span> TOT PTS CINCINNATI REDS - T. MAHLE - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:40 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809012</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809012 </span> <span class='mlb-reds float-l teamnsLogos'></span> TOT PTS CINCINNATI REDS - J. TAILLON - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:40:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809013</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809013 </span> <span class='mlb-pittsburgh-pirates float-l teamnsLogos'></span> TOT PTS PITTSBURGH PIRATES - T. MAHLE - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:40 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809014</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809014 </span> <span class='mlb-pirates float-l teamnsLogos'></span> TOT PTS PITTSBURGH PIRATES - J. TAILLON - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:40:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809031</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809031 </span> <span class='mlb-miami-marlins float-l teamnsLogos'></span> TOT PTS MIAMI MARLINS - T. RICHARDS - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:40 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809032</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809032 </span> <span class='mlb-marlins float-l teamnsLogos'></span> TOT PTS MIAMI MARLINS - J. ARRIETA - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:40:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809033</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809033 </span> <span class='mlb-philadelphia-phillies float-l teamnsLogos'></span> TOT PTS PHILADELPHIA PHILLIES - T. RICHARDS - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:40 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809034</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809034 </span> <span class='mlb-phillies float-l teamnsLogos'></span> TOT PTS PHILADELPHIA PHILLIES - J. ARRIETA - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809051</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809051 </span> <span class='mlb-chicago-cubs float-l teamnsLogos'></span> TOT PTS CHICAGO CUBS - J. QUINTANA - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809052</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809052 </span> <span class='mlb-cubs float-l teamnsLogos'></span> TOT PTS CHICAGO CUBS - CH. ANDERSON - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809053</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809053 </span> <span class='mlb-milwaukee-brewers float-l teamnsLogos'></span> TOT PTS MILWAUKEE BREWERS - J. QUINTANA - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809054</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809054 </span> <span class='mlb-brewers float-l teamnsLogos'></span> TOT PTS MILWAUKEE BREWERS - CH. ANDERSON - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:20:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809071</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809071 </span> <span class='mlb-arizona-diamondbacks float-l teamnsLogos'></span> TOT PTS ARIZONA DIAMONDBACKS - T. WALKER - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:20 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809072</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809072 </span> <span class='mlb-diamondbacks float-l teamnsLogos'></span> TOT PTS ARIZONA DIAMONDBACKS - L. WEAVER - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:20:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809073</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809073 </span> <span class='mlb-louis-cardinals float-l teamnsLogos'></span> TOT PTS ST. LOUIS CARDINALS - T. WALKER - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:20 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809074</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809074 </span> <span class='mlb-cardinals float-l teamnsLogos'></span> TOT PTS ST. LOUIS CARDINALS - L. WEAVER - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>4:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809111</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809111 </span> <span class='mlb-angeles-dodgers float-l teamnsLogos'></span> TOT PTS LOS ANGELES DODGERS - C. KERSHAW - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 4:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809112</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809112 </span> <span class='mlb-dodgers float-l teamnsLogos'></span> TOT PTS LOS ANGELES DODGERS - T. BLACH - L </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>4:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809113</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809113 </span> <span class='mlb-francisco-giants float-l teamnsLogos'></span> TOT PTS SAN FRANCISCO GIANTS - C. KERSHAW - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 4:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809114</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809114 </span> <span class='mlb-giants float-l teamnsLogos'></span> TOT PTS SAN FRANCISCO GIANTS - T. BLACH - L </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>8:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809131</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809131 </span> <span class='mlb-york-mets float-l teamnsLogos'></span> TOT PTS NEW YORK METS - M. HARVEY - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 8:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809132</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809132 </span> <span class='mlb-mets float-l teamnsLogos'></span> TOT PTS NEW YORK METS - T. ROARK - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>8:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809133</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809133 </span> <span class='mlb-washington-nationals float-l teamnsLogos'></span> TOT PTS WASHINGTON NATIONALS - M. HARVEY - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 8:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809134</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809134 </span> <span class='mlb-nationals float-l teamnsLogos'></span> TOT PTS WASHINGTON NATIONALS - T. ROARK - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809151</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809151 </span> <span class='mlb-baltimore-orioles float-l teamnsLogos'></span> TOT PTS BALTIMORE ORIOLES - M. WRIGHT JR. - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809152</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809152 </span> <span class='mlb-orioles float-l teamnsLogos'></span> TOT PTS BALTIMORE ORIOLES - J. MONTGOMERY - L </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809153</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809153 </span> <span class='mlb-york-yankees float-l teamnsLogos'></span> TOT PTS NEW YORK YANKEES - M. WRIGHT JR. - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809154</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809154 </span> <span class='mlb-yankees float-l teamnsLogos'></span> TOT PTS NEW YORK YANKEES - J. MONTGOMERY - L </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809191</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809191 </span> <span class='mlb-city-royals float-l teamnsLogos'></span> TOT PTS KANSAS CITY ROYALS - J. HAMMEL - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809192</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809192 </span> <span class='mlb-royals float-l teamnsLogos'></span> TOT PTS KANSAS CITY ROYALS - M. CLEVINGER - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>1:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809193</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809193 </span> <span class='mlb-cleveland-indians float-l teamnsLogos'></span> TOT PTS CLEVELAND INDIANS - J. HAMMEL - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 1:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809194</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809194 </span> <span class='mlb-indians float-l teamnsLogos'></span> TOT PTS CLEVELAND INDIANS - M. CLEVINGER - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809211</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809211 </span> <span class='mlb-detroit-tigers float-l teamnsLogos'></span> TOT PTS DETROIT TIGERS - M. FIERS - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809212</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809212 </span> <span class='mlb-tigers float-l teamnsLogos'></span> TOT PTS DETROIT TIGERS - R. LOPEZ - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809213</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809213 </span> <span class='mlb-white-sox float-l teamnsLogos'></span> TOT PTS CHICAGO WHITE SOX - M. FIERS - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809214</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809214 </span> <span class='mlb-sox float-l teamnsLogos'></span> TOT PTS CHICAGO WHITE SOX - R. LOPEZ - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809231</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809231 </span> <span class='mlb-seattle-mariners float-l teamnsLogos'></span> TOT PTS SEATTLE MARINERS - M. GONZALES - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809232</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809232 </span> <span class='mlb-mariners float-l teamnsLogos'></span> TOT PTS SEATTLE MARINERS - L. LYNN - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809233</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809233 </span> <span class='mlb-minnesota-twins float-l teamnsLogos'></span> TOT PTS MINNESOTA TWINS - M. GONZALES - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809234</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809234 </span> <span class='mlb-twins float-l teamnsLogos'></span> TOT PTS MINNESOTA TWINS - L. LYNN - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>3:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809251</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809251 </span> <span class='mlb-blue-jays float-l teamnsLogos'></span> TOT PTS TORONTO BLUE JAYS - J. GARCIA - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 3:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809252</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809252 </span> <span class='mlb-jays float-l teamnsLogos'></span> TOT PTS TORONTO BLUE JAYS - C. HAMELS - L </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>3:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809253</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809253 </span> <span class='mlb-texas-rangers float-l teamnsLogos'></span> TOT PTS TEXAS RANGERS - J. GARCIA - L <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 3:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809254</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809254 </span> <span class='mlb-rangers float-l teamnsLogos'></span> TOT PTS TEXAS RANGERS - C. HAMELS - L </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>4:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809271</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809271 </span> <span class='mlb-oakland-athletics float-l teamnsLogos'></span> TOT PTS OAKLAND ATHLETICS - K. GRAVEMAN - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 4:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809272</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809272 </span> <span class='mlb-athletics float-l teamnsLogos'></span> TOT PTS OAKLAND ATHLETICS - S. OHTANI - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>4:10:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809273</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809273 </span> <span class='mlb-angeles-angels float-l teamnsLogos'></span> TOT PTS LOS ANGELES ANGELS - K. GRAVEMAN - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 4:10 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809274</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809274 </span> <span class='mlb-angels float-l teamnsLogos'></span> TOT PTS LOS ANGELES ANGELS - S. OHTANI - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809291</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809291 </span> <span class='mlb-diego-padres float-l teamnsLogos'></span> TOT PTS SAN DIEGO PADRES - T. ROSS - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809292</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809292 </span> <span class='mlb-padres float-l teamnsLogos'></span> TOT PTS SAN DIEGO PADRES - C. MORTON - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1 '>2:15:00</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809293</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4 col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809293 </span> <span class='mlb-houston-astros float-l teamnsLogos'></span> TOT PTS HOUSTON ASTROS - T. ROSS - R <span class='hidden-sm hidden-md hidden-lg pull-right'><i class='fa fa-clock-o'></i> 2:15 PM </span></div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='row'><div class='linesTime row-offset-0 hidden-xs col-xs-1'>PM</div><div class='linesRot row-offset-0 hidden-xs col-xs-1'>809294</div><div class='linesTeam row-offset-0 col-lg-4 col-md-4  col-sm-4 col-xs-12'><span class='hidden-md hidden-lg mobRot'>809294 </span> <span class='mlb-astros float-l teamnsLogos'></span> TOT PTS HOUSTON ASTROS - C. MORTON - R </div><div class='linesSpread row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesMl row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div><div class='linesTotal row-offset-0 col-lg-2 col-md-2 col-sm-2 col-xs-4'>&nbsp;</div></div><div class='lnSeparator'></div></div></div>";
//			final List<SiteEventPackage> siteEvents = betGothamParser.parseGames(xhtml, null, new HashMap<String, String>());
//			for (SiteEventPackage sep : siteEvents) {
//				LOGGER.debug("sep: " + sep);
//			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.wagershack.WagerShackMobileParser#parseMenu(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.info("type: " + java.util.Arrays.toString(type));
		LOGGER.info("sport: " + java.util.Arrays.toString(sport));
		Map<String, String> map = new HashMap<String, String>();

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "submit" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null) && (x < forms.size()); x++) {
			final Element form = forms.get(0);
			if (form != null) {
				// Get form action field
				map.put("action", form.attr("action"));
				getAllElementsByType(form, "input", types, map);
			}			
		}

		// Find the different menu types
		map = findMenu(doc.select(".SportPanel div div"), map, type, sport, "label a", ".c_league");
		
		LOGGER.info("Exiting parseMenu()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <SiteEventPackage> List<SiteEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGames()");
		LOGGER.debug("xhtml: " + xhtml);
		LOGGER.debug("type: " + type);
		LOGGER.debug("InputFields: " + inputFields);
		List<?> events = null;
		
		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		Map<String, String> tempFields = new HashMap<String, String>();

		final String[] types = new String[] { "hidden", "submit" };
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null) && (x < forms.size()); x++) {
			final Element form = forms.get(0);
			if (form != null) {
				// Get form action field
				tempFields.put("action", form.attr("action"));

				// Get all input elements with hidden and submit types
				getAllElementsByType(form, "input", types, tempFields);
				
				Elements elements = form.select("input");
				for (int y = 0; (elements != null) && (y < elements.size()); y++) {
					final Element input = elements.get(y);
					String nameValue = input.attr("name");
					String typeValue = input.attr("type");
					if (nameValue != null && typeValue != null && "checkbox".equals(typeValue)) {
						nameValue = nameValue.trim();
						if (nameValue.contains("linesel_")) {
							tempFields.put(nameValue, "on");
						}
					}
				}
			}
		}

		// Now get the game data
		Elements elements = doc.select(".panel-body div");
		if (elements != null && elements.size() > 0) {
			events = getGameData2(elements);
		}

		LOGGER.info("Exiting parseGames()");
		return (List<SiteEventPackage>)events;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseWagerTypes(String xhtml) throws BatchException {
		LOGGER.info("Entering parseWagerTypes()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Parse to get the Document
		final Document doc = parseXhtml(xhtml);
		
		if (xhtml.contains("alert alert-danger")) {
			final String errorMessage = doc.select(".alert p").html();

			if (errorMessage != null && errorMessage.length() > 0) {
				// Check for a wager limit and change it 
				if (errorMessage.contains("minimum required")) {
					throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, errorMessage, xhtml);
				}
				// Check for a wager limit and change it 
				else if (errorMessage.contains("Max Wager Online Exceeded")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, errorMessage, xhtml);
				}
				// Check for a wager limit and change it 
				else if (errorMessage.contains("line has just changed to")) {
					// Line changed throw exception
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, errorMessage, xhtml);			
				}
				else {
					// Line changed throw exception
					throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, errorMessage, xhtml);								
				}
			}
		} else {
			final String[] types = new String[] { "hidden" };
			getAllElementsByType(doc, "input", types, map);
		}

		LOGGER.info("Exiting parseWagerTypes()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketNumber()");
		String ticketNumber = "";
		final String ticketInfo = "Ticket Number";

		if (xhtml.contains("Your bet expired or has already been submited.")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager has expired or already been bet", xhtml);	
		}
		
		if (xhtml.contains("The line changed for one (or more) of your selections.")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);	
		}

		// First check for Wager has been accepted!
		if (xhtml != null && xhtml.contains(ticketInfo)) {
			// Great, wager is complete; now get the Ticket Number
			// <div id="ticketNumber">136821946</div>
			int index = xhtml.indexOf("Ticket Number :");
			if (index != -1) {
				final String nxhtml = xhtml.substring(index + "Ticket Number :".length()).trim();
				index = nxhtml.indexOf("</strong>");
				if (index != -1) {
					ticketNumber = nxhtml.substring(0, index);
				} else {
					ticketNumber = "Failed to get ticket number";
					throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
				}
			} else {
				ticketNumber = "Failed to get ticket number";
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, ticketNumber, xhtml);
			}
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param elements
	 * @return
	 * @throws BatchException
	 */
	protected List<SiteEventPackage> getGameData2(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		final List<SiteEventPackage> events = new ArrayList<SiteEventPackage>();

		if (elements != null) {
			SiteTeamPackage team1 = null;
			SiteTeamPackage team2 = null;
			SiteEventPackage eventPackage = null;
			String gameDate = null;
			int count = 0;

			for (int x = 0; x < elements.size(); x++) {
				// Loop through the elements and then check for the dates
				final Element element = elements.get(x);

				if (element != null) {
					// Get the date
					final String classInfo = element.attr("class");
					if ((classInfo != null && classInfo.length() > 0) && 
						(classInfo.contains("gameDate"))) {
						LOGGER.debug("element: " + element);
						final Elements divs = element.select(".col-xs-12");
						if (divs != null && divs.size() > 0) {
							final Element div = divs.get(0);
							final String divDate = div.html();
							LOGGER.debug("divDate: " + divDate);
							if (divDate != null && divDate.length() > 0) {
								int index = divDate.indexOf("-");
								if (index != -1) {
									gameDate = divDate.substring(index + 1).trim();
								}
							}
						}
					}

					// Check for a row that has the game
					if ((classInfo != null && classInfo.length() > 0) && 
						(classInfo.equals("row"))) {
						final Elements divs = element.select("div");
						if (divs != null && divs.size() > 0) {
							final Element div = divs.get(0);
							if (!div.html().contains("linesHeader")) {
								if (count == 0) {
									eventPackage = new SiteEventPackage();
									team1 = new SiteTeamPackage();
									parseTeamInfo(div, team1);
									count++;
								} else {
									team2 = new SiteTeamPackage();
									parseTeamInfo(div, team2);

									try {
										// Tue 2/13  06:30PM
										String theDate = "";
										Date newDate = null;
										final Calendar now = Calendar.getInstance();
										int offset = now.get(Calendar.DST_OFFSET);
											String firstPart = gameDate ;
											String secondPart = team1.getTimeofevent() + " " + team2.getTimeofevent() + " " + timeZoneLookup(timezone, offset);
											LOGGER.debug("firstPart: " + firstPart);
											LOGGER.debug("secondPart: " + secondPart);
											theDate = firstPart + " " + String.valueOf(now.get(Calendar.YEAR)) + " " + secondPart;
											LOGGER.debug("theDate: " + theDate);
											newDate = DATE_FORMAT.parse(theDate);

											// Set date of event
											String dateofevent = gameDate + " " + String.valueOf(now.get(Calendar.YEAR)); 
											eventPackage.setDateofevent(dateofevent);
											eventPackage.setEventdatetime(newDate);
											eventPackage.setTimeofevent(secondPart);
											team1.setEventdatetime(newDate);
											team2.setEventdatetime(newDate);
											team1.setDateofevent(gameDate +  " " + String.valueOf(now.get(Calendar.YEAR)));
											team1.setTimeofevent(team1.getTimeofevent() + " " +  team2.getTimeofevent() + " " + timeZoneLookup(timezone, offset));
											team2.setDateofevent(gameDate +  " " + String.valueOf(now.get(Calendar.YEAR)));
											team2.setTimeofevent(team1.getTimeofevent() + " " +  team2.getTimeofevent() + " " + timeZoneLookup(timezone, offset));
									} catch (ParseException pe) {
										LOGGER.error(pe.getMessage(), pe);
										throw new BatchException(AppErrorMessage.SITE_PARSER_EXCEPTION  + " Date parsing exception ");
									}

									// Add the teams
									eventPackage.setSiteteamone(team1);
									eventPackage.setSiteteamtwo(team2);

									// Set the package ID
									eventPackage.setId(Integer.parseInt(team1.getEventid()));

									// Add event
									events.add(eventPackage);

									count = 0;
								}
							}
						}
					}
				} 
			}
		}

		LOGGER.info("Exiting getGameData()");
		return events;
	}

	/**
	 * 
	 * @param div
	 * @param team
	 */
	private void parseTeamInfo(Element div, SiteTeamPackage team) {
		LOGGER.info("Entering parseTeamInfo()");

		// Parse the time
		parseTime(div.select(".linesTime"), team);

		// Parse the rotation id
		parseRot(div.select(".linesRot"), team);

		// Parse the team
		parseTeam(div.select(".linesTeam"), team);

		// Parse the spread
		parseSpread(div.select(".linesSpread"), team);

		// Parse the money line
		parseTotal(div.select(".linesMl"), team);

		// Parse the money line
		parseMoneyLine(div.select(".linesTotal"), team);

		LOGGER.info("Exiting parseTeamInfo()");
	}

	/**
	 * 
	 * @param div
	 * @param team
	 */
	private void parseTime(Elements divs, SiteTeamPackage team) {
		LOGGER.info("Entering parseTime()");

		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String html = div.html();
			if (html != null && html.length() > 0) {
				html = html.trim();
				LOGGER.debug("time: " + html);
				team.setTimeofevent(html);
			}
		}

		LOGGER.info("Exiting parseTime()");
	}

	/**
	 * 
	 * @param div
	 * @param team
	 */
	private void parseRot(Elements divs, SiteTeamPackage team) {
		LOGGER.info("Entering parseRot()");

		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String html = div.html();
			if (html != null && html.length() > 0) {
				html = html.trim();
				LOGGER.debug("eventid: " + html);
				team.setEventid(html);
				try {
					team.setId(Integer.parseInt(html));
				} catch (NumberFormatException nfe) {
					LOGGER.warn(nfe.getMessage(), nfe);
				}
			}
		}

		LOGGER.info("Exiting parseRot()");
	}

	/**
	 * 
	 * @param div
	 * @param team
	 */
	private void parseTeam(Elements divs, SiteTeamPackage team) {
		LOGGER.info("Entering parseTeam()");

		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String html = div.html();
			if (html != null && html.length() > 0) {
				html = html.trim();
				int index = html.indexOf("<span");
				if (index != -1) {
					html = html.substring(0, index);
					index = html.lastIndexOf("</span>");
					if (index != -1) {
						html = html.substring(index + 7);
						LOGGER.debug("team: " + html);
						team.setTeam(html);		
					}
				}
			}
		}

		LOGGER.info("Exiting parseTeam()");
	}

	/**
	 * 
	 * @param divs
	 * @param team
	 */
	private void parseSpread(Elements divs, SiteTeamPackage team) {
		LOGGER.info("Entering parseSpread()");

		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String onclick = div.attr("onclick");
			if (onclick != null && onclick.length() > 0) {
				// 
				// addbet('0','759212','S','NEW YORK METS','1.5','-150','MLB','70','951');
				//
				onclick = onclick.replace("addbet(", "");
				onclick = onclick.replace(");", "");
				final StringTokenizer st = new StringTokenizer(onclick,",");

				String spreadnum = null;
				String spreadid = null;
				String spreadline = null;
				String spreadjuice = null;

				int count = 0;
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					token = token.replace("'", "").trim();
					
					switch (count) {
						case 0:
							spreadnum = token;
							break;
						case 1:
							spreadid = token;
							break;
						case 4:
							spreadline = token;
							break;
						case 5:
							spreadjuice = token;
							break;
						case 2:
						case 3:
						case 6:
						case 7:
						case 8:
						default:
							break;
					}
					count++;
				}

				final StringBuffer id = new StringBuffer(50);
				id.append(spreadnum).append("_");
				id.append(spreadid).append("_");
				id.append(spreadline).append("_");
				id.append(spreadjuice).append("_");

				team.setGameSpreadInputId(id.toString());
				team.setGameSpreadInputName(id.toString());
				team.setGameSpreadInputValue(id.toString());

				final StringBuffer spread = new StringBuffer(10);
				if (spreadnum.startsWith("-")) {
					spread.append(spreadline);
				} else {
					spread.append("+").append(spreadline);
				}
				
				if (spreadjuice.startsWith("-")) {
					spread.append(spreadjuice);
				} else {
					spread.append("+").append(spreadjuice);
				}

				// Setup spread
				team = (SiteTeamPackage)parseSpreadData(reformatValues(spread.toString()), 0, " ", null, team);
			}
		}

		LOGGER.info("Exiting parseSpread()");
	}

	/**
	 * 
	 * @param divs
	 * @param team
	 */
	private void parseTotal(Elements divs, SiteTeamPackage team) {
		LOGGER.info("Entering parseTotal()");

		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String onclick = div.attr("onclick");
			if (onclick != null && onclick.length() > 0) {
				// 
				// addbet('0','759212','S','NEW YORK METS','1.5','-150','MLB','70','951');
				//
				onclick = onclick.replace("addbet(", "");
				onclick = onclick.replace(");", "");
				final StringTokenizer st = new StringTokenizer(onclick,",");

				String totalnum = null;
				String totalid = null;
				String totalindicator = null;
				String totalline = null;
				String totaljuice = null;

				int count = 0;
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					token = token.replace("'", "").trim();
					
					switch (count) {
						case 0:
							totalnum = token;
							break;
						case 1:
							totalid = token;
							break;
						case 2:
							totalindicator = token;
							break;
						case 4:
							totalline = token;
							break;
						case 5:
							totaljuice = token;
							break;
						case 3:
						case 6:
						case 7:
						case 8:
						default:
							break;
					}
					count++;
				}

				final StringBuffer id = new StringBuffer(50);
				id.append(totalnum).append("_");
				id.append(totalid).append("_");
				id.append(totalline).append("_");
				id.append(totaljuice).append("_");

				team.setGameTotalInputId(id.toString());
				team.setGameTotalInputName(id.toString());
				team.setGameTotalInputValue(id.toString());

				final StringBuffer total = new StringBuffer(10);
				if (totalindicator.startsWith("Ov")) {
					total.append("o").append(totalline);
				} else {
					total.append("u").append(totalline);
				}

				if (totaljuice.startsWith("-")) {
					total.append(totaljuice);
				} else {
					total.append("+").append(totaljuice);
				}

				// Setup total
				team = (SiteTeamPackage)parseTotalData(reformatValues(total.toString()), 0, " ", null, team);
			}
		}

		LOGGER.info("Exiting parseTotal()");
	}

	/**
	 * 
	 * @param divs
	 * @param team
	 */
	private void parseMoneyLine(Elements divs, SiteTeamPackage team) {
		LOGGER.info("Entering parseMoneyLine()");

		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String onclick = div.attr("onclick");
			if (onclick != null && onclick.length() > 0) {
				// 
				// addbet('4','759212','ML','NEW YORK METS','0','140','MLB','70','952');
				//
				onclick = onclick.replace("addbet(", "");
				onclick = onclick.replace(");", "");
				final StringTokenizer st = new StringTokenizer(onclick,",");

				String mlnum = null;
				String mlid = null;
				String mlline = null;
				String mljuice = null;

				int count = 0;
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					token = token.replace("'", "").trim();
					
					switch (count) {
						case 0:
							mlnum = token;
							break;
						case 1:
							mlid = token;
							break;
						case 4:
							mlline = token;
						case 5:
							mljuice = token;
							break;
						case 2:
						case 3:
						case 6:
						case 7:
						case 8:
						default:
							break;
					}
					count++;
				}

				final StringBuffer id = new StringBuffer(50);
				id.append(mlnum).append("_");
				id.append(mlid).append("_");
				id.append(mlline).append("_");
				id.append(mljuice).append("_");

				team.setGameMLInputId(id.toString());
				team.setGameMLInputName(id.toString());
				team.addGameMLInputValue("0", id.toString());

				final StringBuffer ml = new StringBuffer(10);				
				if (mljuice.startsWith("-")) {
					ml.append(mljuice);
				} else {
					ml.append("+").append(mljuice);
				}

				// Setup money line
				team = (SiteTeamPackage)parseMlData(reformatValues(ml.toString()), 0, team);
			}
		}

		LOGGER.info("Exiting parseMoneyLine()");
	}
	
	/**
	 * 
	 * @param label
	 * @param total
	 * @param team
	 */
	private void setupTotalData(Element label, String total, SiteTeamPackage team) {
		LOGGER.info("Entering setupTotalData()");

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(label.select("input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameTotalInputId(hashMap.get("id"));
			team.setGameTotalInputName(hashMap.get("name"));
			team.setGameTotalInputValue(hashMap.get("value"));
		}

		// Setup spread
		team = (SiteTeamPackage)parseTotalData(reformatValues(total), 0, " ", null, team);

		LOGGER.info("Exiting setupTotalData()");
	}

	/**
	 * 
	 * @param label
	 * @param ml
	 * @param team
	 */
	private void setupMlData(Element label, String ml, SiteTeamPackage team) {
		LOGGER.info("Entering setupMlData()");

		final Map<String, String> hashMap = parseInputField(label.select("input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameMLInputId(hashMap.get("id"));
			team.setGameMLInputName(hashMap.get("name"));
			team.addGameMLInputValue("0", hashMap.get("value"));
			team = (SiteTeamPackage)parseMlData(reformatValues(ml), 0, team);
		}

		LOGGER.info("Exiting setupMlData()");
	}

	/**
	 * 
	 * @param spread
	 * @param label
	 * @param team
	 */
	private void setupSpreadData(String spread, Element label, SiteTeamPackage team) {
		LOGGER.info("Entering setupSpreadData()");

		// First get the input field information
		final Map<String, String> hashMap = parseInputField(label.select("input"), 0);
		if (hashMap != null && !hashMap.isEmpty()) {
			team.setGameSpreadInputId(hashMap.get("id"));
			team.setGameSpreadInputName(hashMap.get("name"));
			team.setGameSpreadInputValue(hashMap.get("value"));
		}

		// Setup spread
		team = (SiteTeamPackage)parseSpreadData(reformatValues(spread), 0, " ", null, team);
		LOGGER.info("Exiting setupSpreadData()");
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	private String determineIndicator(Element i) {
		LOGGER.info("Entering determineIndicator()");
		String class1 = i.attr("class");

		if (class1 != null && class1.contains("minus")) {
			class1 = "-";
		} else {
			class1 = "+";
		}

		LOGGER.info("Exiting determineIndicator()");
		return class1;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type)
			throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		final Map<String, String> map = new HashMap<String, String>();

		// Check for a wager limit and change it 
		if (xhtml.contains("but this line has just changed to:")) {
			// Line changed throw exception
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed and cannot complete transaction", xhtml);			
		}

		// Parse the HTML first
		final Document doc = parseXhtml(xhtml);

		final String[] types = new String[] { "hidden", "text" };
		Element form = doc.getElementById("aspnetForm");
		if (form != null) {
			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByTypeWithCheckbox(form, "input", types, map);
		}

		// Now get the select options if there are any
		if (type.equals("spread")) {
			setupSpreadWager(doc, siteTeamPackage);
		} else if (type.equals("total")) {
			setupTotalWager(doc, siteTeamPackage);
		} else {
			setupMlWager(doc, siteTeamPackage);
		}

		// Get the wager select
		final Elements selects = doc.select(".teamsNameRotConB select");
		LOGGER.debug("selects: " + selects);
		if (selects != null && selects.size() == 3) {
			parseSelectFieldByNumBlank(selects, 1, map);
		} else if (selects != null && selects.size() == 2) {
			parseSelectFieldByNumBlank(selects, 0, map);
		}

		// Get the pitcher select (if any)
		final Elements pitchers = doc.select(".gPitch select");
		LOGGER.debug("pitchers: " + pitchers);
		if (pitchers != null && pitchers.size() == 2) {
			parseSelectFieldByNumBlank(pitchers, 1, map);
		} else if (pitchers != null && pitchers.size() == 1) {
			parseSelectFieldByNumBlank(pitchers, 0, map);
		}

		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	protected void setupSpreadWager(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupSpreadWager()");

		final Map<String, String> hashMap = parseSelectField(doc.select(".teamsNameRotCon select"));
		if (hashMap != null && hashMap.size() > 0) {
			teamPackage.setGameSpreadSelectName(hashMap.get("name"));
		}

		final Elements options = doc.select(".teamsNameRotCon select option");
		if (options != null && options.size() > 0) {
			LOGGER.debug("options.size(): " + options.size());
			Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
			for (int x = 0; x < optionsMap.size(); x++) {
				final SelectOptionData option = optionsMap.get(Integer.toString(x));
				if (option != null && option.getData() != null && option.getData().length() > 0) {
					String optionData = option.getData();
					optionData = optionData.trim();
					optionData = optionData.replaceAll("â²", "");
					optionData = optionData.replaceAll("Â", "");
					LOGGER.debug("optionData: " + optionData);
					LOGGER.debug("option.getValue(): " + option.getValue());
					teamPackage.addGameSpreadOptionValue(Integer.toString(x), option.getValue());

					// -2½ +200; Now parse the data
					teamPackage = (SiteTeamPackage)parseSpreadData(reformatValues(optionData), x, " ", null, teamPackage);
				}
			}
		} else {
			setupSpreadWagerInfo(doc, teamPackage);
		}

		LOGGER.info("Exiting setupSpreadWager()");
	}

	/**
	 * 
	 * @param doc
	 * @param siteTeamPackage
	 */
	private void setupSpreadWagerInfo(Document doc, SiteTeamPackage siteTeamPackage) {
		LOGGER.info("Entering setupSpreadWagerInfo()");

		// <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 teamsNameRotCon p5">
		//   [52] NEW YORK RANGERS
		//   <span class="pull-right lineSpan">
		//     <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
		//     5
		//     <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
		//     110
		//   </span>
		//   <input id="bp_3_1757496" name="bp_3_1757496" value="0" type="hidden">
		// </div>

		final Elements divs = doc.select(".teamsNameRotCon");
		if (divs != null) {
			for (Element divEle : divs) {
				Elements spans = divEle.select("span");
				if (spans != null && spans.size() > 0) {
					Element span = spans.get(0);
					String html = span.html();
					LOGGER.debug("html: " + html);
					Elements is = span.select("i");
					if (is != null && is.size() == 2) {
						// -1 -110
						String class1 = determineIndicator(is.get(0));
						String class2 = determineIndicator(is.get(1));

						int bindex = html.indexOf("</i>");
						if (bindex != -1) {
							html = html.substring(bindex + 4);
							int eindex = html.indexOf("<i");
							if (eindex != -1) {
								final String line = html.substring(0, eindex).trim();
								html = html.substring(eindex);
								bindex = html.indexOf("</i>");
	
								if (bindex != -1) {
									final String juice = html.substring(bindex + 4).trim();
									final String spread = class1 + line + " " + class2 + juice;
		
									setupSpreadData(spread, span, siteTeamPackage);
								}
							}
						}
					} else if (is != null && is.size() == 1) {
						// PK -110 or -3EV or -3 EV
						html = html.trim();

						if (html.startsWith("PK")) {
							// PK -110
							//     <label style="margin-bottom: 0;">
							//      PK
							//      <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
							//      110
							//      <input id="1_1751773_S_MIAMI OHIO_-3_-110_CFB_2_326" class="checkBox" name="game" type="checkbox">
							//     </label>
							int bindex = html.indexOf("</i>");

							if (bindex != -1) {
								String lineplusminus = "+";
								String line = "0";
								String juiceplusminus = determineIndicator(is.get(0));
								final String juice = html.substring(bindex + 4).trim();
								final String spread = lineplusminus + line + " " + juiceplusminus + juice;
	
								setupSpreadData(spread, span, siteTeamPackage);
							}
						} else {
							// -3 EV or
							//     <label style="margin-bottom: 0;">
							//      <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
							//      3EV
							//      <input id="1_1751773_S_MIAMI OHIO_-3_-110_CFB_2_326" class="checkBox" name="game" type="checkbox">
							//     </label>
							int bindex = html.indexOf("</i>");
							if (bindex != -1) {
								html = html.substring(bindex + 4).trim();
								html = html.replace(" EV", " +100");
								html = html.replace("EV", " +100");
								final String lineplusminus = determineIndicator(is.get(0));
								final String spread = lineplusminus + html;

								setupSpreadData(spread, span, siteTeamPackage);
							}
						}
					} else {
						// PKEV
						// <input id="5_1751768_ML_NORTHWESTERN_0_100_CFB_2_316" class="checkBox" name="game" type="checkbox">
						final String spread = "+0 +100";
						setupSpreadData(spread, span, siteTeamPackage);
					}
				}
			}
		}

		LOGGER.info("Exiting setupSpreadWagerInfo()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	protected void setupTotalWager(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupTotalWager()");
		
		final Map<String, String> hashMap = parseSelectField(doc.select(".teamsNameRotCon select"));
		if (hashMap != null && hashMap.size() > 0) {
			teamPackage.setGameTotalSelectName(hashMap.get("name"));
		}

		final Elements options = doc.select(".teamsNameRotCon select option");
		if (options != null && options.size() > 0) {
			LOGGER.debug("options.size(): " + options.size());
			Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
			for (int x = 0; x < optionsMap.size(); x++) {
				final SelectOptionData option = optionsMap.get(Integer.toString(x));
				if (option != null && option.getData() != null && option.getData().length() > 0) {
					String optionData = option.getData();
					optionData = optionData.trim();
					optionData = optionData.replaceAll("â²", "");
					optionData = optionData.replaceAll("Â", "");
					optionData = optionData.replaceAll("ov", "");
					optionData = optionData.replaceAll("un", "");
					optionData = optionData.replaceAll("▲", "");
					optionData = optionData.replaceAll("▼", "");
					LOGGER.debug("optionData: " + optionData);
					LOGGER.debug("option.getValue(): " + option.getValue());
					teamPackage.addGameTotalOptionValue(Integer.toString(x), option.getValue());

					// -2½ +200; Now parse the data
					teamPackage = (SiteTeamPackage)parseTotalData(reformatValues(optionData), x, " ", null, teamPackage);
				}
			}
		} else {
			setupTotalWagerInfo(doc, teamPackage);
		}

		LOGGER.info("Exiting setupTotalWager()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	private void setupTotalWagerInfo(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupTotalWagerInfo()");

		// <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 teamsNameRotCon p5">
		//   [52] NEW YORK RANGERS
		//   <span class="pull-right lineSpan">
		//     u5½
		//     <i class="glyphicon glyphicon-minus" aria-hidden="true"></i>
		//     110
		//   </span>
		//   <input id="bp_3_1757496" name="bp_3_1757496" value="0" type="hidden">
		// </div>

		final Elements divs = doc.select(".teamsNameRotCon");
		if (divs != null) {
			for (Element divEle : divs) {
				Elements spans = divEle.select("span");
				if (spans != null && spans.size() > 0) {
					Element span = spans.get(0);
					String html = span.html();
					Elements is = span.select("i");
					if (is != null && is.size() == 1) {
						int index = html.indexOf("<i");
						if (index != -1) {
							String total = html.substring(0, index);
							total = total.replace("Ov", "o");
							total = total.replace("Un", "u");
							total = total.trim();
							total = total.replace(" ", "");
							String juiceindicator = determineIndicator(is.get(0));
							
							int bindex = html.indexOf("</i>");
							if (bindex != -1) {
								String juice = html.substring(bindex + 4).trim();
								total = total + " " + juiceindicator + juice;
								setupTotalData(divEle, total, teamPackage);
							}
						}
					} else {
						int index = html.indexOf("EV");
						if (index != -1) {
							String total = html.substring(0, index);
							total = total + " +100";
							setupTotalData(divEle, total, teamPackage);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting setupTotalWagerInfo()");
	}

	/**
	 * 
	 * @param doc
	 * @param teamPackage
	 */
	private void setupMlWager(Document doc, SiteTeamPackage teamPackage) {
		LOGGER.info("Entering setupMlWager()");

		// <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 teamsNameRotCon p5">
		//   [452] CLE BROWNS
		//   <span class="pull-right lineSpan">
		//     <i class="glyphicon glyphicon-plus" aria-hidden="true"></i>
		//     199
		//   </span>
		//   <input id="bp_5_1751821" name="bp_5_1751821" value="0" type="hidden">
		// </div>

		final Elements divs = doc.select(".teamsNameRotCon");
		if (divs != null) {
			for (Element divEle : divs) {
				Elements spans = divEle.select("span");
				if (spans != null && spans.size() > 0) {
					Element span = spans.get(0);
					String html = span.html();
					Elements is = span.select("i");
					if (is != null && is.size() == 1) {
						String ml = null;
						String juiceindicator = determineIndicator(is.get(0));

						int bindex = html.indexOf("</i>");
						if (bindex != -1) {
							ml = html.substring(bindex + 4).trim();
							ml = juiceindicator + ml;
							setupMlData(divEle, ml, teamPackage);
						}
					} else {
						String ml = "+100";
						setupMlData(divEle, ml, teamPackage);
					}
				}
			}
		}

		LOGGER.info("Exiting setupMlWager()");
	}

	/**
	 * 
	 * @param divs
	 * @param map
	 * @param type
	 * @param sport
	 * @param foundString
	 * @param menuString
	 * @return
	 */
	protected Map<String, String> findMenu(Elements divs, Map<String, String> map, String[] type, String[] sport, String foundString, String menuString) {
		LOGGER.info("Entering findMenu()");
		LOGGER.info("divs: " + divs);

		for (int x = 0; (divs != null && x < divs.size()); x++) {
			final Element div = divs.get(x);
			boolean foundDiv = false;
				for (int y = 0; y < type.length; y++) {
					foundDiv = foundSport(div, foundString, type[y]);
					LOGGER.debug("foundDiv: " + foundDiv);
	
					// Found the event
					if (foundDiv) {
						final Elements inputs = div.select(menuString);
						if (inputs != null && inputs.size() > 0) {
							final Element input = inputs.get(0);
							map = new HashMap<String, String>();
							map.put("chk", input.attr("value"));
						}
						LOGGER.debug("Map: " + map);
					}
				}
		}

		LOGGER.info("Exiting findMenu()");
		return map;
	}

	/**
	 * 
	 * @param div
	 * @param select
	 * @param sport
	 * @return
	 */
	protected boolean foundSport(Element div, String select, String sport) {
		LOGGER.info("Entering foundSport()");
		LOGGER.debug("div: " + div);
		LOGGER.debug("select: " + select);
		LOGGER.debug("sport: " + sport);
		boolean foundDiv = false;

		final Elements as = div.select(select);
		if (as != null && as.size() > 0) {
			final Element a = as.get(0);
			LOGGER.debug("a: " + a);
			if (a.html().equals(sport)) {
				foundDiv = true;
			}
		}

		LOGGER.info("Exiting foundSport()");
		return foundDiv;
	}
}