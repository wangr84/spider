package com.study.common;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpiderConfigTool {

	private String configPath = "/Spider.xml";
	private File file;
	private SAXReader saxReader;
	private Document doc;
	private Node spiderNode;

	public SpiderConfigTool(String spiderName) throws DocumentException, UnsupportedEncodingException {
		saxReader = new SAXReader();
//		file = new File(this.getClass().getResourceAsStream("/Spider.xml"));
		InputStream is = this.getClass().getResourceAsStream("/Spider.xml");
		Reader reader = new InputStreamReader(is, "utf-8");
//		Map<String, String> namespaceURIs = new HashMap<String, String>();
//		namespaceURIs.put("cnml", "http://www.cnml.org.cn/2005/CNMLSchema");
//		namespaceURIs.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
//		saxReader.getDocumentFactory().setXPathNamespaceURIs(namespaceURIs);
		try {
			doc = saxReader.read(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}

		spiderNode = getSpider(spiderName);

	}

	public Node getSpiderNode(){
		return spiderNode;
	}

	@SuppressWarnings("unchecked")
	private Node getSpider(String spiderName){
		List<Node> list = doc.selectNodes("config/spider-cofig");

		for(Node i : list){
			Node domain = i.selectSingleNode("domain");
			if (domain != null && domain.getText().equals(spiderName)) {
				return i;
			}
		}

		return null;
	}

	public Document getDoc(){
		return doc;
	}

}
