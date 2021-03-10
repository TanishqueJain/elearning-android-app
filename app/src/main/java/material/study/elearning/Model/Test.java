package material.study.elearning.Model;

public class Test {
    private String Testname, Testtime;

    public Test() {
    }

    public Test(String testname, String testtime) {
        Testname = testname;
        Testtime = testtime;
    }

    public String getTestname() {
        return Testname;
    }

    public void setTestname(String testname) {
        Testname = testname;
    }

    public String getTesttime() {
        return Testtime;
    }

    public void setTesttime(String testtime) {
        Testtime = testtime;
    }
}

