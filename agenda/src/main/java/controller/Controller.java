package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns = { "/controller", "/main", "/insert", "/select", "/update", "/delete", "/report" })
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controller() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		System.out.println(action);
		if (action.equals("/main")) {
			try {
				contatos(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equals("/insert")) {
			try {
				novoContatos(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equals("/select")) {
			try {
				listarContatos(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equals("/update")) {
			try {
				editarContatos(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} else if (action.equals("/delete")) {
			try {
				removerContatos(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} else if (action.equals("/report")) {
			try {
				gerarRelatorio(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}		
		} else {
			response.sendRedirect("index.html");
		}
	}

	// Listar contatos
	protected void contatos(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Criando um objeto que irá receber os dados JavaBeans
		ArrayList<JavaBeans> lista = dao.listarContatos();
		// Encaminhar a lista do documento Agenda.jsp
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("Agenda.jsp");
		rd.forward(request, response);
	}

	// Novo contato
	protected void novoContatos(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// setar as variáveis JavaBeans
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		// invocar o método inserirContato passando o objeto contato
		dao.inserirContato(contato);
		// redirecionar para o documento Agenda.jsp
		response.sendRedirect("main");
	}

	
	protected void listarContatos(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Recebimento do id do contato que será editado
		String idcon = request.getParameter("idcon");
		// Setar a variável JavaBeans
		contato.setIdcon(idcon);
		// Executar o método selecionarContato (DAO)
		dao.selecionarContato(contato);
		// Setar os atributos do formulário com o contéudo JavaBeans
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());
		// Encaminhar ao documento Editar.jsp
		RequestDispatcher rd = request.getRequestDispatcher("Editar.jsp");
		rd.forward(request, response);
	}
	
	protected void editarContatos(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Setar as variáveis JavaBeans
		contato.setIdcon(request.getParameter("idcon"));
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		// executar método alterarContato
		dao.alterarContato(contato);
		// redirecionar para o documento Agenda.jsp (atualizando as alterações)
		response.sendRedirect("main");
	}
	
	// Remover contato
	protected void removerContatos(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// Recebimento do id a ser excluído
		String idcon = request.getParameter("idcon");
		// Setar as variável idcon JavBeans
		contato.setIdcon(idcon);
		// Executar o método deletarContato (DAO)
		dao.deletarContato(contato);
		// redirecionar para o documento Agenda.jsp (atualizando as alterações)
		response.sendRedirect("main");
	}
	
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Document documento = new Document();
		try {
			// tipo de conteúdo
			response.setContentType("application/pdf");
			// nome do documento
			response.addHeader("Content-Disposition", "inline; filename=" + "contatos.pdf");
			// criar o documento
			PdfWriter.getInstance(documento, response.getOutputStream());
			// abrir o documento
			documento.open();
			documento.add(new Paragraph("Lista de contatos:"));
			documento.add(new Paragraph(" "));
			// criar uma tabela
			PdfPTable tabela = new PdfPTable(3);
			// cabeçalho
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome:"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Fone:"));
			PdfPCell col3 = new PdfPCell(new Paragraph("E-mail:"));
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);
			// popular tabela com os contatos
			ArrayList<JavaBeans> lista = dao.listarContatos();
			for (int i = 0; i < lista.size(); i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getFone());
				tabela.addCell(lista.get(i).getEmail());
			}
			documento.add(tabela);
			documento.close();
		} catch (Exception e) {
			System.out.println(e);
			documento.close();
		}
		
	}
	
	
	
	
	
	
	

}
