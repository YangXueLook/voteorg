import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument.Iterator;

/**
 * 
 * @author yaohucaizi
 */
public class VoteTest {

	/**
	 * 读取网页全部内容
	 */
	public String getHtmlContent(String htmlurl) {
		URL url;
		String temp;
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL(htmlurl);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream(), "UTF-8"));// 读取网页全部内容
			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			in.close();
		} catch (final MalformedURLException me) {
			System.out.println("你输入的URL格式有问题!");
			me.getMessage();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param s
	 * @return 获得网页标题
	 */
	public String getTitle(String s) {
		String regex;
		String title = "";
		List<String> list = new ArrayList<String>();
		regex = "<title>.*?</title>";
		Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
		Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		for (int i = 0; i < list.size(); i++) {
			title = title + list.get(i);
		}
		return outTag(title);
	}

	/**
	 * 
	 * @param s
	 * @return 获得链接
	 */
	public List<String> getLink(String s) {
		String regex;
		List<String> list = new ArrayList<String>();
		regex = "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>";
		Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		return list;
	}

	/**
	 * 
	 * @param s
	 * @return 获得脚本代码
	 */
	public List<String> getScript(String s) {
		String regex;
		List<String> list = new ArrayList<String>();
		regex = "<SCRIPT.*?</SCRIPT>";
		Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		return list;
	}

	public List<String> getNews(String s) {
		String regex = "<a.*?</a>";
		Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		Matcher ma = pa.matcher(s);
		List<String> list = new ArrayList<String>();
		while (ma.find()) {
			list.add(ma.group());
		}
		return list;
	}

	/**
	 * 
	 * @param s
	 * @return 获得CSS
	 */
	public List<String> getCSS(String s) {
		String regex;
		List<String> list = new ArrayList<String>();
		regex = "<style.*?</style>";
		Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		return list;
	}

	/**
	 * 
	 * @param s
	 * @return 去掉标记
	 */
	public String outTag(final String s) {
		return s.replaceAll("<.*?>", "");
	}

	public String getLastStage(String url) {
		String content = getHtmlContent(url);

		content = content.replaceAll("(<br>)+?", "\n");// 转化换行

		content = content.replaceAll("<p><em>.*?</em></p>", "");// 去图片注释

		int stagesIndex = content.indexOf("Stages");
		content = content.substring(stagesIndex);
		int tailIndex = content.indexOf("</a>");
		content = content.substring(0, tailIndex);
		return content;
	}

	public ArrayList<String> getLastStageList(String content) {
		ArrayList<String> linksList = (ArrayList<String>) getLink(content);
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < linksList.size(); i++) {
			String s = linksList.get(i);
			if (s.contains("href=\"/bill"))

			{
				int firstIndex = s.indexOf("href=\"") + 6;
				int secondIndex = s.indexOf("\">");
				s = s.substring(firstIndex, secondIndex);
				s = "http://votesmart.org//" + s;
				result.add(getLastStage(s));

			}

		}
		return result;
	}

	boolean isPassed(String lastStage) {
		if (lastStage.contains("Sent To Committee"))
			return false;
		else if (lastStage.contains("Nonconcurrence")
				&& lastStage.contains("Passed"))
			return false;
		else if (lastStage.contains("Failed"))
			return false;
		else if (lastStage.contains("Veto"))
			return false;
		else if (lastStage.contains("Rejected"))
			return false;
		else if (lastStage.contains("Not")&&lastStage.contains("Pass")&&lastStage.contains("Adopted"))
			return false;
		else
			return true;
	}

	public static void main(String[] args) {
		VoteTest t = new VoteTest();
		HashMap<String, VoteResult> NoAndVoteResultMap = new HashMap<String, VoteResult>();
		ArrayList<VoteResult> passedVote = new ArrayList<VoteResult>();
		ArrayList<VoteResult> showList = new ArrayList<VoteResult>();
		//  HI "IL" "IA""MT","NH", 
		//"AL", "AK", "AZ", "AR","CA",AR,"MA", "MD", "MI","LA","ME","MN", "MS", "MO",
		//"CO", "CT", "DE", "FL", "GA", "HI" "ID", "IL","IN","KS", "KY","NE", "NV", "NJ", "NM", "NY", "NC",
		String[] stateArray = new String[] {   
				    
				  "ND", "OH",
				"OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT",
				"VA", "WA", "WV", "WI", "WY" };

		for (int stateIndex = 0; stateIndex < stateArray.length; stateIndex++) {
			NoAndVoteResultMap.clear();
			passedVote.clear();
			showList.clear();
			for (int year = 2014; year >= 2007; year--) {

//				String url = "http://votesmart.org/bills/"
//						+ stateArray[stateIndex] + "/" + year;
				String url = "http://www.k-state.edu";
				String content = t.getHtmlContent(url);
				System.out.println(content);
				content = content.replaceAll("(<br>)+?", "\n");// 转化换行

				content = content.replaceAll("<p><em>.*?</em></p>", "");// 去图片注释

				ArrayList<String> lastStageList = t.getLastStageList(content);

				content = t.outTag(content);
				
//				

				String ss1 = "About the Selection and Descriptions of Key Votes";
				int firstCutIndex = content.indexOf(ss1) + ss1.length();
				content = content.substring(firstCutIndex);

				String ss2 = "Outcome";
				int startIndex = content.indexOf(ss2) + ss2.length();
				int endIndex = content.indexOf(ss1);
				content = content.substring(startIndex, endIndex);

				// System.out.println(content);

				content = content.replaceAll("  *  ", "---");

				for (int i = 0; i < 10; i++) {
					content = content.replaceAll(i + "\\)---", i + "\\)\n");
				}

				content = content.replaceAll("---", "\t");
				content = content.substring(1);

				String[] array = content.split("\n");

				for (int i = 0; i < array.length; i++) {
					if (array[i].contains("Failed")||array[i].contains("Rejected"))
						continue;

//					 System.out.println(array[i]);
					
					if(lastStageList.size() == 0)
						continue;

					if (t.isPassed(lastStageList.get(i))) {
						String[] subArray = array[i].split("\t");

						if (subArray.length != 6)
							continue;

						if(subArray[1].length() < 4)
							continue;
						
						String billNo = subArray[1].substring(3);
						if (NoAndVoteResultMap.containsKey(billNo)) {
							VoteResult doublePassed = NoAndVoteResultMap
									.get(billNo);
							if (subArray[4].contains("Senate")) {
								doublePassed.sPassed = true;
								doublePassed.bothPassed = true;
								String voteNo = subArray[5];
								voteNo = voteNo.replace("(", "");
								voteNo = voteNo.replace(")", "");
								String[] voteNoArray = voteNo.split(" - ");
								doublePassed.sYes = Integer
										.parseInt(voteNoArray[0]);
								doublePassed.sNays = Integer
										.parseInt(voteNoArray[1]);
								passedVote.add(doublePassed);
							}
							if (subArray[4].contains("House")) {
								doublePassed.hPassed = true;
								doublePassed.bothPassed = true;
								String voteNo = subArray[5];
								voteNo = voteNo.replace("(", "");
								voteNo = voteNo.replace(")", "");
								String[] voteNoArray = voteNo.split(" - ");
								doublePassed.hYes = Integer
										.parseInt(voteNoArray[0]);
								doublePassed.hNays = Integer
										.parseInt(voteNoArray[1]);
								passedVote.add(doublePassed);
							}
						}

						else {

							VoteResult v = new VoteResult();
							v.date = subArray[0] + "/" + year;
							v.state = subArray[1].substring(0, 2);
							v.billNo = billNo;
							v.billTitle = subArray[2];
							v.outcome = subArray[3];
							if (subArray[4].contains("Senate")) {
								v.sPassed = true;

								String voteNo = subArray[5];

								voteNo = voteNo.replace("(", "");

								voteNo = voteNo.replace(")", "");

								String[] voteNoArray = voteNo.split(" - ");
								v.sYes = Integer.parseInt(voteNoArray[0]);
								v.sNays = Integer.parseInt(voteNoArray[1]);
							}
							if (subArray[4].contains("House")) {
								v.hPassed = true;

								String voteNo = subArray[5];

								voteNo = voteNo.replace("(", "");

								voteNo = voteNo.replace(")", "");

								String[] voteNoArray = voteNo.split(" - ");
								v.hYes = Integer.parseInt(voteNoArray[0]);
								v.hNays = Integer.parseInt(voteNoArray[1]);

							}
							NoAndVoteResultMap.put(billNo, v);
						}
					}

				}

			}

			java.util.Iterator<String> it = NoAndVoteResultMap.keySet()
					.iterator();

			while (it.hasNext()) {
				showList.add(NoAndVoteResultMap.get(it.next()));

			}

			Collections.sort(showList, new Comparator<VoteResult>() {

				@Override
				public int compare(VoteResult o1, VoteResult o2) {
					String[] array1 = o1.date.split("/");
					if (array1[0].length() == 1)
						array1[0] = "0" + array1[0];
					if (array1[1].length() == 1)
						array1[1] = "0" + array1[1];

					String[] array2 = o2.date.split("/");
					if (array2[0].length() == 1)
						array2[0] = "0" + array2[0];
					if (array2[1].length() == 1)
						array2[1] = "0" + array2[1];

					String s1 = array1[2] + array1[0] + array1[1];

					String s2 = array2[2] + array2[0] + array2[1];

					return Integer.parseInt(s1) - Integer.parseInt(s2);
				}
			});

			System.out.println("Date" + "\t" + "State" + "\t" + "Bill No."
					+ "\t" + "Bill Title" + "\t" + "Outcome" + "\t"
					+ "House Yeas" + "\t" + "House Nays" + "\t" + "Senate Yeas"
					+ "\t" + "Senate Nays");

			for (int i = showList.size() - 1; i >= 0; i--) {
				VoteResult v = showList.get(i);
				System.out.println(v.date + "\t" + v.state + "\t" + v.billNo
						+ "\t" + v.billTitle + "\t" + v.outcome + "\t" + v.hYes
						+ "\t" + v.hNays + "\t" + v.sYes + "\t" + v.sNays);

			}
			System.out.println("**************************************");

		}

		//
		// for(int i = 0; i < linksList.size(); i++)
		// {
		// String s = linksList.get(i);
		// if(s.contains("href=\"/bill"))
		//
		// {
		// int firstIndex = s.indexOf("href=\"")+6;
		// int secondIndex = s.indexOf("\">");
		// s = s.substring(firstIndex, secondIndex);
		// s = "http://votesmart.org//" + s;
		// System.out.println(s);
		//
		// System.out.println(t.getLastStage(s));
		// }
		//
		// }

		// System.out.println(content);

		// System.out.println(array.length);

		// System.out.println(t.getTitle(content));
		// List<String> a = t.getNews(content);
		// List<String> news = new ArrayList<String>();
		// for (String s : a) {
		// news.add(s.replaceAll("<.*?>", ""));
		// }
		// System.out.println(news);
		// …… 获取js、css等操作省略
	}

}

class VoteResult {
	String date;
	String state;
	String billNo;
	String billTitle;
	String outcome;
	boolean bothPassed = false;
	boolean hPassed = false;
	int hYes = 0;
	int hNays = 0;
	boolean sPassed = false;
	int sYes = 0;
	int sNays = 0;

	VoteResult() {
	}

}