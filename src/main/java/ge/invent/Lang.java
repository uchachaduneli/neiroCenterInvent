package ge.invent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 */
@ViewScoped
@ManagedBean(name = "langBean")
public class Lang implements Serializable {

    public static String ka(String str) {
        if (str != null) {
            str = str.replace("R", "ღ");
            str = str.replace("j", "ჯ");
            str = str.replace("u", "უ");
            str = str.replace("k", "კ");
            str = str.replace("e", "ე");
            str = str.replace("n", "ნ");
            str = str.replace("g", "გ");
            str = str.replace("S", "შ");
            str = str.replace("w", "წ");
            str = str.replace("z", "ზ");
            str = str.replace("f", "ფ");
            str = str.replace("Z", "ძ");
            str = str.replace("v", "ვ");
            str = str.replace("a", "ა");
            str = str.replace("p", "პ");
            str = str.replace("r", "რ");
            str = str.replace("o", "ო");
            str = str.replace("l", "ლ");
            str = str.replace("W", "ჭ");
            str = str.replace("C", "ჩ");
            str = str.replace("y", "ყ");
            str = str.replace("s", "ს");
            str = str.replace("m", "მ");
            str = str.replace("i", "ი");
            str = str.replace("t", "ტ");
            str = str.replace("q", "ქ");
            str = str.replace("b", "ბ");
            str = str.replace("h", "ჰ");
            str = str.replace("d", "დ");
            str = str.replace("J", "ჟ");
            str = str.replace("x", "ხ");
            str = str.replace("c", "ც");
            str = str.replace("T", "თ");

            return str;
        } else {
            return null;
        }
    }
}
