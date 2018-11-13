package com.company;

public class Main {
    public static void main(String[] args) throws Exception {
        INIFile ini = new INIFile();
        String input = "test.ini";
        if (args.length > 0)
            input = args[0];

        ini.readFile(input);
        System.out.println(ini);
        INISection values = ini.getSection("VALUES");
        String[] dataType = new String[]{"int", "double", "str", "none"};
        for (String s: dataType) {
            System.out.print(String.format("Getting %12s as String: ", s));
            try {
                System.out.print(values.getProperty(s, new String().getClass()));
            } catch(Exception ex) {
                System.out.print("(Error)");
            }
            System.out.print(", as Int: ");
            try {
                System.out.print(values.getProperty(s, new Integer(1).getClass()));
            } catch(Exception ex) {
                System.out.print("(Error)");
            }
            System.out.print(", as Double: ");
            try {
                System.out.print(values.getProperty(s, new Double(1).getClass()));
            } catch(Exception ex) {
                System.out.print("(Error)");
            }
            System.out.println(".");
        }
        for (String sectionName: ini.keys()) {
            INISection section = ini.getSection(sectionName);
            if (section.size() == 0)
                ini.delSection(sectionName);
        }

        INISection section = ini.addSection("Facts");
        section.addProperty("Light", "Kira");
        section.addProperty("Life", "isDance");
        section = ini.addSection("Relationships");
        section.addProperty("Kira", "Misa");
        ini.writeFile("output.ini");
    }
}

