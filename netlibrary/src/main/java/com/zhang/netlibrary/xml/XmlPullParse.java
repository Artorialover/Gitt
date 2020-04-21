package com.zhang.netlibrary.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

public class XmlPullParse {
    public static void parse(String content) throws XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(new StringReader(content));
        if (parser.getEventType() == XmlPullParser.START_DOCUMENT) {
            System.out.println("====Start Document====");
        }
        while (parser.getEventType()!=XmlPullParser.END_DOCUMENT){
            switch (parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    break;
                case XmlPullParser.TEXT:
                    break;
                case XmlPullParser.END_TAG:
                    break;
                default:
                    break;
            }
        }
        if (parser.getEventType() == XmlPullParser.END_DOCUMENT) {
            System.out.println("====End Document====");
        }
    }
}
