package com.changestuffs.server.guice.graph;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Test;

import com.changestuffs.client.core.articles.UpdateArticleView;
import com.changestuffs.client.core.components.ChatView;
import com.changestuffs.client.core.lookfor.OffersView;
import com.changestuffs.client.gin.ClientModule;
import com.changestuffs.client.widget.social.SocialView;
import com.changestuffs.client.widget.texteditor.TextEditorView;
import com.changestuffs.server.guice.GuiceServletConfig;
import com.google.gwt.inject.rebind.adapter.GinModuleAdapter;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.grapher.GrapherModule;
import com.google.inject.grapher.InjectorGrapher;
import com.google.inject.grapher.graphviz.GraphvizModule;
import com.google.inject.grapher.graphviz.GraphvizRenderer;
import com.google.inject.util.Modules;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class GrapherTest {

	private final String FILE_GUICE = "docs/guiceGraph.dot";
	private final String FILE_GIN = "docs/ginGraph.dot";
	private Injector injectorGuice;
	private Injector injectorGin;

	@Before
	public void before() throws Exception {
		injectorGuice = new GuiceServletConfig().getInjector();
		GinModuleAdapter adapter = new GinModuleAdapter(new ClientModule());
		injectorGin = Guice.createInjector(Modules.override(adapter).with(new GinToGuiceModule()));
	}

	@Test
	public void makeGraph() throws IOException {
		graphGood(FILE_GUICE, injectorGuice);
		graphGood(FILE_GIN, injectorGin);
	}

	public final static Injector graphGood(String filename, Injector inj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter(baos);

		Injector injector = Guice.createInjector(new GrapherModule(),
				new GraphvizModule());
		GraphvizRenderer renderer = injector
				.getInstance(GraphvizRenderer.class);
		renderer.setOut(out).setRankdir("TB");

		injector.getInstance(InjectorGrapher.class).of(inj).graph();

		out = new PrintWriter(new File(filename), "UTF-8");
		String s = baos.toString("UTF-8");
		s = fixGrapherBug(s);
		s = hideClassPaths(s);
		out.write(s);
		out.close();

		return inj;
	}

	public static String hideClassPaths(String s) {
		s = s.replaceAll("\\w[a-z\\d_\\.]+\\.([A-Z][A-Za-z\\d_]*)", "");
		s = s.replaceAll("value=[\\w-]+", "random");
		return s;
	}

	public static String fixGrapherBug(String s) {
		s = s.replaceAll("style=invis", "style=solid");
		return s;
	}
	
	private static class GinToGuiceModule extends AbstractModule {

		private final Logger log = Logger.getLogger(getClass().getName());

		@Override
		protected void configure() {
			bind(UpdateArticleView.Binder.class).to(getMakeSon(UpdateArticleView.Binder.class));
			bind(TextEditorView.Binder.class).to(getMakeSon(TextEditorView.Binder.class));
			bind(SocialView.Binder.class).to(getMakeSon(SocialView.Binder.class));
			bind(OffersView.Binder.class).to(getMakeSon(OffersView.Binder.class));
			bind(ChatView.Binder.class).to(getMakeSon(ChatView.Binder.class));
			bind(Proxy.class).to(getMakeSon(Proxy.class));
		}

		private <T> TypeLiteral<? extends T> getMakeSon(Class<T> clazz) {
			@SuppressWarnings("unchecked")
			TypeLiteral<? extends T> literal = (TypeLiteral<? extends T>) TypeLiteral.get(mock(clazz).getClass());
			log.info(literal.getType() + " is subclass of " + clazz);
			return literal;
		}
	}

}
