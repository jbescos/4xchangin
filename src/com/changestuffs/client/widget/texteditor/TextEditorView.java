package com.changestuffs.client.widget.texteditor;

import com.changestuffs.client.i18n.ChangestuffsMessages;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RichTextArea.Formatter;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class TextEditorView extends ViewImpl implements
		TextEditorPresenter.MyView {

	private final static ChangestuffsMessages messages = GWT.create(ChangestuffsMessages.class);

	// HTML Related (styles made by SPAN and DIV)
	private static final String HTML_STYLE_CLOSE_SPAN = "</span>";
	private static final String HTML_STYLE_CLOSE_DIV = "</div>";
	private static final String HTML_STYLE_OPEN_BOLD = "<span style=\"font-weight: bold;\">";
	private static final String HTML_STYLE_OPEN_ITALIC = "<span style=\"font-weight: italic;\">";
	private static final String HTML_STYLE_OPEN_UNDERLINE = "<span style=\"font-weight: underline;\">";
	private static final String HTML_STYLE_OPEN_LINETHROUGH = "<span style=\"font-weight: line-through;\">";
	private static final String HTML_STYLE_OPEN_ALIGNLEFT = "<div style=\"text-align: left;\">";
	private static final String HTML_STYLE_OPEN_ALIGNCENTER = "<div style=\"text-align: center;\">";
	private static final String HTML_STYLE_OPEN_ALIGNRIGHT = "<div style=\"text-align: right;\">";
	private static final String HTML_STYLE_OPEN_INDENTRIGHT = "<div style=\"margin-left: 40px;\">";

	// HTML Related (styles made by custom HTML-Tags)
	private static final String HTML_STYLE_OPEN_SUBSCRIPT = "<sub>";
	private static final String HTML_STYLE_CLOSE_SUBSCRIPT = "</sub>";
	private static final String HTML_STYLE_OPEN_SUPERSCRIPT = "<sup>";
	private static final String HTML_STYLE_CLOSE_SUPERSCRIPT = "</sup>";
	private static final String HTML_STYLE_OPEN_ORDERLIST = "<ol><li>";
	private static final String HTML_STYLE_CLOSE_ORDERLIST = "</ol></li>";
	private static final String HTML_STYLE_OPEN_UNORDERLIST = "<ul><li>";
	private static final String HTML_STYLE_CLOSE_UNORDERLIST = "</ul></li>";

	// HTML Related (styles without closing Tag)
	private static final String HTML_STYLE_HLINE = "<hr style=\"height: 2px;\">";

	// GUI Related stuff
	private static final String GUI_DIALOG_INSERTURL = messages.enterLink();

	private final Widget widget;
	private final Formatter styleTextFormatter;
	
	@UiField
	ToggleButton bold;
	@UiField
	ToggleButton italic;
	@UiField
	ToggleButton underline;
	@UiField
	ToggleButton stroke;
	@UiField
	ToggleButton subscript;
	@UiField
	ToggleButton superscript;
	@UiField
	ToggleButton texthtml;
	@UiField
	PushButton alignleft;
	@UiField
	PushButton alignmiddle;
	@UiField
	PushButton alignright;
	@UiField
	PushButton orderlist;
	@UiField
	PushButton unorderlist;
	@UiField
	PushButton indentleft;
	@UiField
	PushButton indentright;
	@UiField
	PushButton generatelink;
	@UiField
	PushButton breaklink;
	@UiField
	PushButton insertline;
	@UiField
	PushButton removeformatting;
	@UiField
	PushButton youtube;
	@UiField
	ListBox fontlist;
	@UiField
	ListBox colorlist;
	@UiField
	RichTextArea styleText;
	@UiField
	FlowPanel uploadPanel;

	public interface Binder extends UiBinder<Widget, TextEditorView> {
	}

	@Inject
	public TextEditorView(final Binder binder) {
		GWT.log("TextEditorView instanced");
		widget = binder.createAndBindUi(this);
		styleTextFormatter = styleText.getFormatter();
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@UiHandler("bold")
	public void bold(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_BOLD, HTML_STYLE_CLOSE_SPAN);
		} else {
			styleTextFormatter.toggleBold();
		}
		updateStatus();
	}

	@UiHandler("italic")
	public void italic(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_ITALIC, HTML_STYLE_CLOSE_SPAN);
		} else {
			styleTextFormatter.toggleItalic();
		}
		updateStatus();
	}

	@UiHandler("underline")
	public void underline(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_UNDERLINE, HTML_STYLE_CLOSE_SPAN);
		} else {
			styleTextFormatter.toggleUnderline();
		}
		updateStatus();
	}

	@UiHandler("stroke")
	public void stroke(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_LINETHROUGH, HTML_STYLE_CLOSE_SPAN);
		} else {
			styleTextFormatter.toggleStrikethrough();
		}
		updateStatus();
	}

	@UiHandler("subscript")
	public void subscript(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_SUBSCRIPT,
					HTML_STYLE_CLOSE_SUBSCRIPT);
		} else {
			styleTextFormatter.toggleSubscript();
		}
		updateStatus();
	}

	@UiHandler("superscript")
	public void superscript(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_SUPERSCRIPT,
					HTML_STYLE_CLOSE_SUPERSCRIPT);
		} else {
			styleTextFormatter.toggleSuperscript();
		}
		updateStatus();
	}

	@UiHandler("alignleft")
	public void alignleft(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_ALIGNLEFT, HTML_STYLE_CLOSE_DIV);
		} else {
			styleTextFormatter
					.setJustification(RichTextArea.Justification.LEFT);
		}
		updateStatus();
	}

	@UiHandler("alignmiddle")
	public void alignmiddle(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_ALIGNCENTER, HTML_STYLE_CLOSE_DIV);
		} else {
			styleTextFormatter
					.setJustification(RichTextArea.Justification.CENTER);
		}
		updateStatus();
	}

	@UiHandler("alignright")
	public void alignright(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_ALIGNRIGHT, HTML_STYLE_CLOSE_DIV);
		} else {
			styleTextFormatter
					.setJustification(RichTextArea.Justification.RIGHT);
		}
		updateStatus();
	}

	@UiHandler("orderlist")
	public void orderlist(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_ORDERLIST,
					HTML_STYLE_CLOSE_ORDERLIST);
		} else {
			styleTextFormatter.insertOrderedList();
		}
		updateStatus();
	}

	@UiHandler("unorderlist")
	public void unorderlist(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_UNORDERLIST,
					HTML_STYLE_CLOSE_UNORDERLIST);
		} else {
			styleTextFormatter.insertUnorderedList();
		}
		updateStatus();
	}

	@UiHandler("indentleft")
	public void indentleft(ClickEvent event) {
		if (isHTMLMode()) {
			// TODO nothing can be done here at the moment
		} else {
			styleTextFormatter.leftIndent();
		}
		updateStatus();
	}

	@UiHandler("indentright")
	public void indentright(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_OPEN_INDENTRIGHT, HTML_STYLE_CLOSE_DIV);
		} else {
			styleTextFormatter.rightIndent();
		}
		updateStatus();
	}

	@UiHandler("generatelink")
	public void generatelink(ClickEvent event) {
		String url = Window.prompt(GUI_DIALOG_INSERTURL, "http://");
		if (url != null) {
			if (isHTMLMode()) {
				changeHtmlStyle("<a href=\"" + url + "\">", "</a>");
			} else {
				styleTextFormatter.createLink(url);
			}
		}
		updateStatus();
	}
	
	@UiHandler("youtube")
	public void youtube(ClickEvent event) {
		String url = Window.prompt(GUI_DIALOG_INSERTURL, "http://");
		if (url != null) {
			final String HTML_YOUTUBE_START="<iframe width=\"640\" height=\"390\" frameborder=\"0\" title=\"YouTube video player\" class=\"youtube-player\" type=\"text/html\" src=\""+url+"\" allowfullscreen=\"\">";
			final String HTML_YOUTUBE_END = "</iframe>";
			final String HTML_YOUTUBE = HTML_YOUTUBE_START+HTML_YOUTUBE_END;
			if (isHTMLMode()) {
				changeHtmlStyle(HTML_YOUTUBE_START, HTML_YOUTUBE_END);
			} else {
				styleTextFormatter.insertHTML(HTML_YOUTUBE);
			}
		}
		updateStatus();
	}

	@UiHandler("breaklink")
	public void breaklink(ClickEvent event) {
		if (isHTMLMode()) {
			// TODO nothing can be done here at the moment
		} else {
			styleTextFormatter.removeLink();
		}
		updateStatus();
	}

	@UiHandler("insertline")
	public void insertline(ClickEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(HTML_STYLE_HLINE, "");
		} else {
			styleTextFormatter.insertHorizontalRule();
		}
		updateStatus();
	}

	@UiHandler("removeformatting")
	public void removeformatting(ClickEvent event) {
		if (isHTMLMode()) {
			// TODO nothing can be done here at the moment
		} else {
			styleTextFormatter.removeFormat();
		}
		updateStatus();
	}

	@UiHandler("texthtml")
	public void texthtml(ClickEvent event) {
		if (texthtml.isDown()) {
			styleText.setText(styleText.getHTML());
		} else {
			styleText.setHTML(styleText.getText());
		}
		updateStatus();
	}

	@UiHandler("fontlist")
	public void fontlist(ChangeEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(
					"<span style=\"font-family: "
							+ fontlist.getValue(fontlist.getSelectedIndex())
							+ ";\">", HTML_STYLE_CLOSE_SPAN);
		} else {
			styleTextFormatter.setFontName(fontlist.getValue(fontlist
					.getSelectedIndex()));
		}
	}

	@UiHandler("colorlist")
	public void colorlist(ChangeEvent event) {
		if (isHTMLMode()) {
			changeHtmlStyle(
					"<span style=\"color: "
							+ colorlist.getValue(colorlist.getSelectedIndex())
							+ ";\">", HTML_STYLE_CLOSE_SPAN);
		} else {
			styleTextFormatter.setForeColor(colorlist.getValue(colorlist
					.getSelectedIndex()));
		}
	}

	/**
	 * Private method with a more understandable name to get if HTML mode is on
	 * or not
	 **/
	private Boolean isHTMLMode() {
		return texthtml.isDown();
	}

	/** Method called to toggle the style in HTML-Mode **/
	private void changeHtmlStyle(String startTag, String stopTag) {
		JsArrayString tx = getSelection(styleText.getElement());
		String txbuffer = styleText.getText();
		Integer startpos = Integer.parseInt(tx.get(1));
		String selectedText = tx.get(0);
		styleText.setText(txbuffer.substring(0, startpos) + startTag
				+ selectedText + stopTag
				+ txbuffer.substring(startpos + selectedText.length()));
	}

	/**
	 * Private method to set the toggle buttons and disable/enable buttons which
	 * do not work in html-mode
	 **/
	private void updateStatus() {
		if (styleTextFormatter != null) {
			bold.setDown(styleTextFormatter.isBold());
			italic.setDown(styleTextFormatter.isItalic());
			underline.setDown(styleTextFormatter.isUnderlined());
			subscript.setDown(styleTextFormatter.isSubscript());
			superscript.setDown(styleTextFormatter.isSuperscript());
			stroke.setDown(styleTextFormatter.isStrikethrough());
		}

		if (isHTMLMode()) {
			removeformatting.setEnabled(false);
			indentleft.setEnabled(false);
			breaklink.setEnabled(false);
		} else {
			removeformatting.setEnabled(true);
			indentleft.setEnabled(true);
			breaklink.setEnabled(true);
		}
	}

	/**
	 * Native JavaScript that returns the selected text and position of the
	 * start
	 **/
	public static native JsArrayString getSelection(Element elem) /*-{
		var txt = "";
		var pos = 0;
		var range;
		var parentElement;
		var container;

		if (elem.contentWindow.getSelection) {
			txt = elem.contentWindow.getSelection();
			pos = elem.contentWindow.getSelection().getRangeAt(0).startOffset;
		} else if (elem.contentWindow.document.getSelection) {
			txt = elem.contentWindow.document.getSelection();
			pos = elem.contentWindow.document.getSelection().getRangeAt(0).startOffset;
		} else if (elem.contentWindow.document.selection) {
			range = elem.contentWindow.document.selection.createRange();
			txt = range.text;
			parentElement = range.parentElement();
			container = range.duplicate();
			container.moveToElementText(parentElement);
			container.setEndPoint('EndToEnd', range);
			pos = container.text.length - range.text.length;
		}
		return [ "" + txt, "" + pos ];
	}-*/;

	@Override
	public void putImageUrl(String url, String alt) {
		if (url != null) {
			if (isHTMLMode()) {
				changeHtmlStyle("<img src='" + url + "' alt='"+alt+"'>", "");
			} else {
				GWT.log("Interting image: "+url+" alt: "+alt);
				styleTextFormatter.insertHTML("<img alt=\""+alt+"\" src=\""+url+"\"/>");
			}
		}
		updateStatus();
	}

	@Override
	public String getHtml() {
		String html = styleText.getHTML();
		return html;
	}

	@Override
	public void setHtml(String html) {
		styleText.setHTML(html);
	}

	@Override
	public void addUploadPanel(UploadView uploadView) {
		uploadPanel.add(uploadView);
	}

}
