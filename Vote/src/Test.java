import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author yaohucaizi
 */
public class Test {

	/**
	 * ��ȡ��ҳȫ������
	 */
	public String getHtmlContent(String htmlurl) {
		URL url;
		String temp;
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL(htmlurl);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream(), "gbk"));// ��ȡ��ҳȫ������
			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			in.close();
		} catch (final MalformedURLException me) {
			System.out.println("�������URL��ʽ������!");
			me.getMessage();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param s
	 * @return �����ҳ����
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
	 * @return �������
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
	 * @return ��ýű�����
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
	 * @return ���CSS
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
	 * @return ȥ�����
	 */
	public String outTag(final String s) {
		return s.replaceAll("<.*?>", "");
	}

	public static void main(String[] args) {
		Test t = new Test();
		String content = t.getHtmlContent("http://www.taobao.com");
		// content = content.replaceAll("(<br>)+?", "\n");// ת������
		// content = content.replaceAll("<p><em>.*?</em></p>", "");// ȥͼƬע��
		System.out.println(content);
		System.out.println(t.getTitle(content));
		List<String> a = t.getNews(content);
		List<String> news = new ArrayList<String>();
		for (String s : a) {
			news.add(s.replaceAll("<.*?>", ""));
		}
		System.out.println(news);
		// ���� ��ȡjs��css�Ȳ���ʡ��
	}
}
