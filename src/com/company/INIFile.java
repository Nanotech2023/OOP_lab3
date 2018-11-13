package com.company;

import java.util.HashMap;
import java.io.*;
import java.util.zip.DataFormatException;

public class INIFile {
    private HashMap<String, INISection> sections_;
    public INIFile() {
        sections_ = new HashMap<String, INISection>();
    }
    public INISection getSection(String sectionName) {
        return sections_.get(sectionName);
    }

    public void readFile(String fileName) throws FileNotFoundException, DataFormatException, IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = file.readLine();
            if (line == null)
                break;
            sb.append(line);
            sb.append("\n");
        }
        file.close();
        parse(sb.toString());
    }

    public void parse(String iniToParse) throws DataFormatException {
        int pos = 0;
        if (!iniToParse.endsWith("\n"))
            iniToParse += "\n";
        while (true) {
            int posNext = iniToParse.indexOf("\n", pos);
            if (posNext == -1)
                break;
            String line = iniToParse.substring(pos, posNext);
            if (line.isEmpty() || Utils.deleteSpaces(line).charAt(0) == ';') {
                pos = posNext + 1;
                continue;
            }
            if (line.charAt(0) == '[') {
                boolean ok = line.contains("]");
                for (int i = line.indexOf(']') + 1; i < line.length(); ++i)
                    if (line.charAt(i) == ';')
                        break;
                    else if (line.charAt(i) != ' ') {
                        ok = false;
                        break;
                    }
                if (!ok)
                    throw new DataFormatException(String.format("Error while reading ini file: label \"%s\" is broken", line));
                String name = line.substring(1, line.indexOf(']'));
                if (name.contains(" "))
                    throw new DataFormatException(String.format("Error while reading ini file: label \"%s\" contain spaces", name));
                int posFinish = iniToParse.indexOf("\n[", posNext) != -1 ?
                        iniToParse.indexOf("\n[", posNext) :
                        iniToParse.length();
                addSection(new INISection(name, iniToParse.substring(posNext, posFinish)));
                posNext = posFinish;
            }
            pos = posNext + 1;
        }
    }

    public INISection addSection(String sectionName) throws DataFormatException {
        sections_.put(sectionName, new INISection(sectionName));
        return sections_.get(sectionName);
    }

    public INISection addSection(INISection section) {
        sections_.put(section.getName(), section);
        return sections_.get(section.getName());
    }

    public void delSection(String sectionName) {
        sections_.remove(sectionName);
    }

    public String[] keys() {
        String[] ans = new String[sections_.size()];
        int i = 0;
        for (String sectName: sections_.keySet())
            ans[i++] = sectName;
        return ans;
    }

    public int size() {
        return sections_.size();
    }

    public void clear() {
        sections_ = new HashMap<String, INISection>();
    }

    public void writeFile(String fileName) throws IOException {
        BufferedWriter file = new BufferedWriter(new FileWriter(fileName));
        int i = 0;
        for (String section: sections_.keySet()) {
            INISection out = sections_.get(section);
            file.write(String.format("[%s]\n", section));
            for (String prop: out.keys())
                file.write(String.format("%s = %s\n", prop, out.getProperty(prop)));
            if (++i < sections_.size())
                file.write("\n");
        }
        file.close();
    }

    public String toString() {
        String ans = "[[[\n";
        int i = 0;
        for (String label: sections_.keySet()) {
            ans += label + ": {";
            int j = 0;
            INISection section = sections_.get(label);
            for (String entry: section.keys())
                ans += String.format("%s=%s%s", entry, section.getProperty(entry), (j++ == section.size() - 1 ? "}" : ", "));
            if (section.size() == 0)
                ans += "}";
            ans += (i++ == sections_.size() - 1 ? "\n]]]" : ";\n");
        }
        if (sections_.size() == 0)
            ans += "]]]";
        return ans;
    }
}
