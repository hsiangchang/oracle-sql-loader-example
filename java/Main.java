
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 產生測試用的檔案：隨機產生各一百萬筆的資料檔案
 * 
 * @author HSIANG
 */
public class Main {

	/** 常見的姓氏 */
	private final static String[] surnames = { "陳", "林", "黃", "張", "李", "王", "吳", "劉", "蔡", "楊", "許", "鄭", "謝", "郭",
			"洪", "曾", "邱", "廖", "賴", "周", "徐", "蘇", "葉", "莊", "呂", "江", "何", "蕭", "羅", "高", "簡", "朱", "鍾", "施", "游",
			"詹", "沈", "彭", "胡", "余", "盧", "潘", "顏", "梁", "趙", "柯", "翁", "魏", "方", "孫", "張簡", "戴", "范", "歐陽", "宋", "鄧",
			"杜", "侯", "曹", "薛", "傅", "丁", "溫", "紀", "范姜", "蔣", "歐", "藍", "連", "唐", "馬", "董", "石", "卓", "程", "姚", "康",
			"馮", "古", "姜", "湯", "汪", "白", "田", "涂", "鄒", "巫", "尤", "鐘", "龔", "嚴", "韓", "黎", "阮", "袁", "童", "陸", "金",
			"錢", "邵" };

	public static void main(String[] args) {
		int size = surnames.length;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SecureRandom s = new SecureRandom();
		int idx, totalCount = 1000000;
		String name, line;
		Calendar birth;
		try (BufferedWriter out = new BufferedWriter(new FileWriter("vendors.dat"))) {
			for (int i = 1; i <= totalCount; i++) {
				idx = s.nextInt(size);
				name = surnames[idx] + getLengthBig5(2);
				birth = Calendar.getInstance();
				birth.add(Calendar.DATE, -1 * s.nextInt(365 * 50));
				line = rightPad("" + i, 10, "0") + rightPad(name, 20, " ") + sdf.format(birth.getTime());
				out.append(line);
				out.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try (BufferedWriter out = new BufferedWriter(new FileWriter("users.dat"))) {
			for (int i = 1; i <= totalCount; i++) {
				idx = s.nextInt(size);
				name = surnames[idx] + getLengthBig5(2);
				birth = Calendar.getInstance();
				birth.add(Calendar.DATE, -1 * s.nextInt(365 * 50));
				line = i + "," + name + "," + sdf.format(birth.getTime());
				out.append(line);
				out.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("FINISH");
	}

	/**
	 * 隨機產生中文字
	 * 
	 * @return
	 */
	public static String getBig5() {
		String str = null;
		int highPos, lowPos;
		SecureRandom random = new SecureRandom();
		highPos = Integer.valueOf("A4", 16)
				+ Math.abs(random.nextInt(Integer.valueOf("C6", 16) - Integer.valueOf("A4", 16)));
		lowPos = Integer.valueOf("40", 16)
				+ Math.abs(random.nextInt(Integer.valueOf("7E", 16) - Integer.valueOf("40", 16)));
		byte[] b = new byte[2];
		b[0] = (new Integer(highPos)).byteValue();
		b[1] = (new Integer(lowPos)).byteValue();
		try {
			str = new String(b, "BIG5");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 取得固定長度的中文字
	 * 
	 * @param length
	 * @return
	 */
	public static String getLengthBig5(int length) {
		String str = "";
		for (int i = length; i > 0; i--) {
			str = str + getBig5();
		}
		return str;
	}

	/**
	 * 補足位元數
	 * 
	 * @param s
	 * @param len
	 * @param c
	 * @return
	 */
	public static String rightPad(String s, int len, String c) {
		int size = len - s.getBytes().length;
		for (int i = 0; i < size; i++) {
			s += c;
		}
		return s;
	}
}
