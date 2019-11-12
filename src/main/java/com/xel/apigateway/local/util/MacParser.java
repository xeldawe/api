package com.xel.apigateway.local.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MacParser {

	public List<String> macParser(List<String> list) {
		return listMacParser(list, 1);
	}

	public String macParser(String str) {
		return macParser(str, 1);
	}

	public List<String> listMacParser(List<String> list, int mode) {
		Pattern macFilter = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
		final String pattern;
		final String format;
		switch (mode) {
		case 1:
			pattern = "(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
			format = "$1:$2:$3:$4:$5:$6";
			break;
		case 2:
			pattern = "(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
			format = "$1-$2-$3-$4-$5-$6";
			break;
		case 3:
			pattern = "(\\d{3})(\\d{3})(\\d{3})(\\d{3})";
			format = "$1:$2:$3:$4";
			break;
		case 4:
			pattern = "(\\d{3})(\\d{3})(\\d{3})(\\d{3})";
			format = "$1-$2-$3-$4";
			break;
		case 5:
			pattern = "(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
			format = "$1.$2.$3.$4.$5.$6";
			break;
		case 6:
			pattern = "(\\d{12})";
			format = "$1";
			break;
		default:
			return null;
		}
		list = list.stream().filter(macFilter.asPredicate()).collect(Collectors.toList());

		list.stream().forEach(item -> {
			item.replaceAll("(\\.|\\,|\\:|\\-)", "");
			item.replaceFirst(pattern, format).toLowerCase();
		});
		return list;
	}

	public String macParser(String str, int mode) {
		System.out.println("Mac - Parser input: "+str);
		@SuppressWarnings("unused")
		List<String> list = new ArrayList<>();
		list.add(str);
		Pattern macFilter = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
		final String pattern;
		final String format;
		switch (mode) {
		case 1:
			pattern = "(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})";
			format = "$1:$2:$3:$4:$5:$6";
			break;
		case 2:
			pattern = "(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})";
			format = "$1-$2-$3-$4-$5-$6";
			break;
		case 3:
			pattern = "(.{3})(.{3})(.{3})(.{3})";
			format = "$1:$2:$3:$4";
			break;
		case 4:
			pattern = "(.{3})(.{3})(.{3})(.{3})";
			format = "$1-$2-$3-$4";
			break;
		case 5:
			pattern = "(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})";
			format = "$1.$2.$3.$4.$5.$6";
			break;
		case 6:
			pattern = "(.{12})";
			format = "$1";
			break;
		default:
			return null;
		}

		list = list.stream().filter(macFilter.asPredicate()).collect(Collectors.toList());
		list.stream().forEach(item -> {
			item.replaceAll("(\\.|\\,|\\:|\\-)", "");
			item.replaceFirst(pattern, format).toLowerCase();
		});
		if(list.size() > 0) {
			System.out.println("Mac - Parser output: "+list.get(0));
		return list.get(0);
		}else {
			System.out.println("Mac - Parser output: null");
			return null;
		}
	}

}
