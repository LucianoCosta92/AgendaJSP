/**
 * Confirmação de exlusão de um contato
 * @author Luciano Costa dos Santos
 * @param idcon
 */

function confirmar(idcon){
	let resposta = confirm("Confirma a exclusão desde contato?")
	if(resposta === true){
		window.location.href = "delete?idcon=" + idcon
	}
}