package cn.ibona.commonutil.pingyin;

public class SortModel {

    private String name;   //显示的数据
    private String letter;  //显示数据拼音的首字母
    private int id;

    public SortModel()
    {}
    public SortModel(String inName, int inId)
    {
        setName(inName);
        setId(inId);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}