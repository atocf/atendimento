${lum_beforeWrite('<script type="text/javascript" src="lumis/tool/jquery/jquery.js"></script>', 'jquery.js')}

<%
#lum_include('/br/com/bmg/blog/theme/bmg-blog/www/js/normaliza.js')

function listarCategorias(categoria) { 
	if (categoria != "undefined") {
		var termos = categoria.serviceInstance[0].terms.term;
		var tam = termos.length;
		var str = "";

		str += termos[0].name;

		return str;
	} else return ''
		}

function getDate(date) {
	var mes = String(date.monthName);
	var ano = date.year;

	var mesCaptalize = mes.charAt(0).toUpperCase() + mes.slice(1)
	return mesCaptalize + ' ' + ano;
}

var rows = lum_xpath.getMaps("//data/row");

for(var i in rows){
	var post = rows[i];

	var categoria = post.categorization.serviceInstances
	%>

<script>
	
	
	
	
var onSubmit = function(token) {
		return new Promise(function(resolve, reject) {  
			var form = $("#newsletter__form");
			form.find(".g-recaptcha-response").val(token);
			form.submit();
			resolve()
			grecaptcha.reset(clientId);
		})
	};
	
	var clientId = true;
	
	var BMGDatalayer = {
		"siteInfo": {
			"acronym":"blog",
			"section":"Blog",
			"brand": "BMG"
		},
		"pageInfo": {
			"name":"bmg|web|nl|blog|<%=normalizar(listarCategorias(categoria))%>|<%=normalizar(String(post.title))%>",
			"query":window.location.search,
			"hash" :  window.location.href.match(/#([^#]+)/)==null?'':window.location.href.match(/#([^#]+)/)[0],
			"referrer" : document.referrer,
			"path" : window.location.pathname
		},
		"sessionInfo": {
			"user": {
				"login_status" : "NL"
			}
		}
	}
	
	function formataNumberApi(str){
		if(str!=null){
			return parseFloat(str.replace(/\D+/g, ''));
		}
		return "";
	}
</script>

<main id="main" class="main">
	<section class="section breadcumb-section">
		<div class="wrapper ">
			<div class="content">
				<ol class="breadcrumb-new " itemscope
					itemtype="https://schema.org/BreadcrumbList">
					<li itemprop="itemListElement" itemscope
						itemtype="https://schema.org/ListItem">
						<a class="breadcrumb__item-new tp1--link tp1-desk--link" itemprop="item"
						   href="/blog">
							<span itemprop="name">Início</span></a>
						<meta itemprop="position" content="1" />
					</li>
					<li itemprop="itemListElement" itemscope
						itemtype="https://schema.org/ListItem">
						<a class="breadcrumb__item-new tp1--link tp1-desk--link" itemprop="item"
						   href='/blog/<%=normalizar(listarCategorias(categoria))%>'>
							<span
								  itemprop="name"><%=(categoria  != undefined) ? listarCategorias(categoria) : "" %></span></a>
						<meta itemprop="position" content="2" />
					</li>
					<li class="breadcrumb__item-new tp1--link tp1-desk--link"
						itemprop="itemListElement" itemscope itemtype="https://schema.org/ListItem">
						<span itemprop="name"><%=post.title%></span>
						<meta itemprop="position" content="3" />
					</li>
				</ol>
			</div>
		</div>
	</section>

	<!-- IMAGE FULL -->
	<!--TEMPLATE PARA COMPONENTES NOVOS-->
	<% if(post.contentImage){ %>
	<section class="section--image">
		<div class="wrapper ">
			<div class="content" >
				<picture class="image--fill" aria-hidden="true">
					<source media="(max-width: 1019px)" srcset="<%=post.contentImage.href%>">
					<source media="(min-width: 1020px)" srcset="<%=post.contentImage.href%>">
					<img alt="<%=post.contentImage.mediaLegend%>" src="<%=post.contentImage.href%>">
				</picture>
			</div>
		</div>
	</section>
	<% } %>

	<!-- // IMAGE FULL -->

	<!-- PostContet -->
	<!--TEMPLATE PARA COMPONENTES NOVOS-->
	<section class="section newsletter" id="fixedBox">
		<div class="wrapper ">
			<div class="content" >
				<div class="postContent">

					<div class="target__outside" data-changePosition="socialVertical" id="sharePost">
						<!-- socialVerticalBar -->
						<ul class="socialVerticalBar" id="socialVertical">
							<span class="socialVerticalBar__text tp1--body1 show-mobile" >
								Compartilhe 
							</span>
							<li class="socialVerticalBar__item">        
								<a class="socialVerticalBar__link link" href="https://www.facebook.com/sharer/sharer.php?u=" target="_blank" title="Compartilhe no Facebook" aria-label="Compartilhe no Facebook" target="_blank">
								<span class="icomoon-facebook"  role="img" aria-label="Ícone do Facebook"></span>
								</a>
							</li>

							<li class="socialVerticalBar__item">
								<a class="socialVerticalBar__link link" href="https://twitter.com/intent/tweet?url=" target="_blank" title="Compartilhe no Twitter" aria-label="Compartilhe no Twitter">
								<span class="icomoon-twitter"  role="img" aria-label="Ícone do Twitter"></span>
								</a>            
							</li>

							<li class="socialVerticalBar__item">
								<a class="socialVerticalBar__link link" href="https://www.linkedin.com/shareArticle?mini=true&url=" target="_blank" title="Compartilhe no Linkedin" aria-label="Compartilhe no Linkedin">
								<span class="icomoon-linkedin"  role="img" aria-label="Ícone do Linkedin"></span>

								</a>        
							</li>

							<li class="socialVerticalBar__item">        
								<a class="socialVerticalBar__link link" href="https://api.whatsapp.com/send?text=" target="_blank" title="Compartilhe no Whatsapp" aria-label="Compartilhe no Whatsapp">
								<span class="icomoon-whatsapp"  role="img" aria-label="Ícone do Whatsapp"></span>

								</a>
							</li>
						</ul>
						<!-- socialVerticalBar -->
					</div>

					<div class="postText">
						<p >
							<%=getDate(String(post.publishStartDate))  %>
							<%=(categoria  != undefined) ? " | " : "" %>  <a href='/blog/<%=normalizar(listarCategorias(categoria))%>'> <%=(categoria  != undefined) ? listarCategorias(categoria) : "" %> </a>
						</p>

						<h1>
							<%=post.title%>
						</h1>
						<p>
							<%
   							var postContent = String(post.content).replace(/<[^>]+>/g, '') || '';
							  var wordsCount = postContent.replace(/(^\s*)|(\s*$)/gi,"")
									.replace(/[ ]{2,}/gi," ")
									.replace(/\n /,"\n")
									.split(' ').length;

								var tempoLeitura = Math.floor(wordsCount/130);
							%>
							<%= 'Tempo de leitura: '+  (tempoLeitura === 1 ? '1 minuto' : tempoLeitura > 1 ?  tempoLeitura + ' minutos' : 'menos de 1 minuto') %>
						</p>
						
						<% lum_out.print(post.content) %>
						
						<div id="bannerPost" class="m-t-space-l" data-categoria="<%=(categoria  != undefined) ? listarCategorias(categoria) : "" %>"></div>
					</div>
					<input type="hidden" value="<%=post.id%>" id="idContent"/>
					<input type="hidden" value="<%=post.contentId%>" id="idContentAssocia"/>
				
					
					<%
						var h2s = (String(post.content) || '').replace(/\r?\n|\r/g," ").match(/<\s*h2\s*[^>]*>(.*?)<\s*\/\s*h2\s*>/g);

						var topicos = []

						if (h2s) {
							for (var index = 0; index < h2s.length; index++) {
								var element = h2s[index];
								var matchid = element.match(/id=["'](.*?)["']/)

								var innerText = element.replace(/<[^>]+>/g, '') 
								.replace(/(^\s*)|(\s*$)/gi,"")
								.replace(/[ ]{2,}/gi," ")
								
								if (matchid) {
								  var id =matchid[1]
								  if (id) {
									topicos.push('<a class="tp1--body3 link" href="#' + id + '" data-topico="'+ id + '" >' + innerText + '</a>')
								  } 
								} else {
									topicos.push('<a class="tp1--body3 link" href="#' + innerText.toLowerCase() + '" data-topico="'+ innerText.toLowerCase() + '" >' + innerText + '</a>')
								}
							  }
							}

							if (topicos.length) {
							%>
								<div class="newsletter-fixed desktop-only"
								  id="postNavigationCard" >
								  <div class="navegacao-rapida">
									<div class="navegacao-rapida__box">
									  <h3 class="navegacao-rapida__title tp1-desk--display">
										Navegação por tópicos
									  </h3>
									  <div class="navegacao-rapida__topicos " data-changePosition="links-topicos-navegacao" id="links-topicos-navegacao">
										<% 
										  for (var topicoIndex = 0; topicoIndex < topicos.length; topicoIndex++) {
										  	lum_out.print(topicos[topicoIndex])
										  }
										 %>
										
									  </div>
									</div>
								  </div>
								</div>
							<% 

							}
						%>
		
					
					
					
					</div>
				</div>

			</div>
		</div>

	</section>
	<div id="removeFixed"></div>


	
	<section class="section show-mobile section__show-mobile">
		<div class="wrapper ">
			<div class="content" >
			<div class="target__inside" data-changePosition="socialVertical">

			</div>
				<%
				if (topicos.length) {
				%>
				<div id="postNavigationCardMobile">
					<div class="navegacao-rapida">
					<div class="accordion" tabindex="0">
					  <div class="accordion__item" id="sobreBanco">
						<button class="accordion__text tp1--display" aria-expanded="false" title="Navegação por tópicos" tabindex="0">
						  Navegação por tópicos
						  <span class=" accordion__action-text visually-hidden">
							Toque para expandir
						  </span>
						</button>
						<div class="accordion__wrapper" id="accordionNavegacaoTopicos" aria-expanded="false">
						  <div class="accordion__content">
							  <div class="navegacao-rapida__box">
								<div class="target__inside" data-changePosition="links-topicos-navegacao">
								</div>
							  </div>
							</div>
						  </div>
						</div>
					  </div>
					</div>
          </div>
				<% 
				}
				%>
			</div>
		</div>
	</section>

	<section class="section section--gray section--rating" id="rating">
		<div class="wrapper">
			<div class="content rating__content">
				<div>
					<div class="box-voto">
						<h5 class="rating__title tp1--headline5">
							O que achou deste conteúdo?
						</h5>
						<input type="hidden" id="post-id" value="140">
						<div id="vote" class="rateit rateit-bg" data-rateit-value="1">
							<button id="rateit-reset-2" type="button" data-role="none" class="rateit-reset" aria-label="reset rating" aria-controls="rateit-range-2" style="display: block;">
								<span></span>
							</button>
						</div>
					</div>
					<div class="box-mensagem esconde">
						<h5 class="rating__title tp1--headline5">Obrigado pelo seu voto</h5>
						<a class="mostra-media" href="javascript:void(0)" title="Ver nota média para essa postagem">Ver nota média para essa postagem</a>
					</div>
					<div class="box-media esconde">
						<h5 class="rating__title tp1--headline5">A nota média desse post é 
							<span class="nota" id="nota"></span>
							<span class="total" id="total"></span>
						</h5>
						<div id="media" class="rateit rateit-bg" value="" data-rateit-readonly="true">
							<button id="rateit-reset-3" type="button" data-role="none" class="rateit-reset" aria-label="reset rating" aria-controls="rateit-range-3" style="display: none;">
								<span></span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	
	<section class="section">
		<div class="wrapper">
			<div class="content">
				<div class="newsletter">
					<div class="newsletter__box">
						<div class="newsletter__header">
							<h3 class="newsletter__title tp1--body1 tp1-desk--headline3">
								Receba novos conteúdos direto<br class="show-desktop">em seu e-mail
							</h3>
							<img class="newsletter__img show-mobile" src="/lumis-theme/br/com/bmg/blog/theme/bmg-blog/img/icons/newsletterLittle.svg">
						</div>
						<form id="newsletter__form" class="newsletter__form newsletter__content">
							<div class="one-line-desktop">
								<div class="input__wrapper">
									<div class="input__field">
										<label class="input__label tp1--body3" for="nome">
											Nome
										</label>
										<input class="input tp1--body1" type="text" id="nome" placeholder="Nome" name="nome" maxlength="50" required="">
										<button class="input__icon input__close" type="button">
											<img src="/lumis-theme/br/com/bmg/blog/theme/bmg-blog/img/icons/clear.svg" alt="Aperte para limpar" title="Aperte para limpar">
										</button>
									</div>
								</div>
								<div class="input__wrapper">
									<div class="input__field">
										<label class="input__label tp1--body3" for="email">
											E-mail
										</label>
										<input class="input tp1--body1 input--email" type="email" id="email" placeholder="E-mail" name="email" required="">
										<button class="input__icon input__close" type="button">
											<img src="/lumis-theme/br/com/bmg/blog/theme/bmg-blog/img/icons/clear.svg" alt="Aperte para limpar" title="Aperte para limpar">
										</button>
									</div>
								</div>
							</div>
							<p class="newsletter__caption tp1--caption">
								Ao assinar a newsletter, declaro que conheço a <a class="link" href="#">Política de Privacidade</a> e autorizo a utilização das minhas informações pelo Banco BMG.
							</p>
							<div class="input__wrapper">
								<div class="input__field">
									<button type="submit" class="btn tp1--button3  open-sucess g-recaptcha"
											role="button" title="Quero me cadastrar" 
											id="captcha" data-loading="false">
										Quero me cadastrar
									</button>
								</div>
							</div>
						</form>
					</div>
					<picture class="newsletter__figure show-desktop" aria-hidden="true">
						<source media="(max-width: 1019px)" srcset="/lumis-theme/br/com/bmg/blog/theme/bmg-blog/img/icons/newsletterBig.svg">
						<source media="(min-width: 1020px)" srcset="/lumis-theme/br/com/bmg/blog/theme/bmg-blog/img/icons/newsletterBig.svg">
						<img alt="imagem newsletter" src="/lumis-theme/br/com/bmg/blog/theme/bmg-blog/img/icons/newsletterBig.svg">
					</picture>
				</div>
			</div>
		</div>
	</section>
	
	<!-- POSTS RELACIONADOS -->
	<section class="section section_popular_posts section__posts-relacionados @@sectionClass" id="postsRelacionados">
		<div class="wrapper">
			<div class="content">
				<div class="content__grid grid-12">
					<h3 class="section__title tp1--headline4"> Posts Relacionados</h3>
					<div class="swiper-container card_blog__popular__mobile">
						<div class="swiper-wrapper" id="postsRelacionadosMobile">
						</div>
					</div>
					<div class="card_blog__popular__wrapper card_blog__popular__desktop" id="postsRelacionadosDesktop">
					</div>
				</div>
			</div>
		</div>
	</section>

</main>
<% } %>

<script>
	$(document).ready(function () {
		
		// HighLight Navigation Bar
		var h2sElements = document.querySelectorAll('.postContent h2');
		
		if (h2sElements.length) {
		var navigationHeight = innerWidth < 1220 ? innerHeight - document.querySelector('#postNavigationCardMobile .navegacao-rapida').offsetHeight : innerHeight;
		var headerHeight = document.querySelector('#header').offsetHeight +   (innerWidth < 1220 ? 5 : 10);
		var bottomOfScreen = navigationHeight;
		var topOfScreen = headerHeight;
		
		function isInViewport( element, sibling){
			var elementRect = element.getBoundingClientRect();
			var topOfElement = sibling ? elementRect.top : elementRect.top + ( innerWidth < 1220 ? 82 : 92);
			var bottomOfElement = elementRect.bottom;
   		return (bottomOfScreen > topOfElement) && (topOfScreen < bottomOfElement)
		}
		
		function checkAllSiblings( elements){
			var hasElementInView = false;
			$(elements).each(function( index, value ) {
				if (hasElementInView) {
					return false;
				}
			  	hasElementInView = isInViewport(value, true)
			})
			return hasElementInView;
		}
		
		var seletores = {};
		var ultimosConteudos = {};
		
		$('#links-topicos-navegacao a').each(function( index, value ) {
			var id = normalizar(value.getAttribute("href").replace(/\s/g, '-')).toLowerCase();
			value.setAttribute('href', '#' + id)
			value.setAttribute('data-topico', id)
		})
		
		for (var index = 0; index < h2sElements.length; index++) {
			var h2Element = h2sElements[index]
			if (!h2Element.id) {
	 			h2sElements[index].id = normalizar(h2Element.innerText.replace(/\s/g, '-')).toLowerCase()
			}
			var nextH2 = h2sElements[index + 1];
			
			var conteudoH2;
			if (h2Element.id) {
				if (nextH2) {
					conteudoH2 = $(h2Element).nextUntil(nextH2)
				} else {
					var lastElement = document.querySelector('.postText').lastElementChild
					conteudoH2 = $(h2Element).nextUntil(lastElement)
				}
				ultimosConteudos[h2Element.id] = conteudoH2;
				seletores[h2Element.id] = document.querySelector('[data-topico="' + h2Element.id +'"]' ) 
			}
		}		
		
		document.addEventListener( 'scroll', function(event) {
			var hasElementHighlighted = false;
			for (var index = 0; index < h2sElements.length; index++) {
				var myElement = h2sElements[index];
				var lastElement = ultimosConteudos[myElement.id]
				
				if( ( isInViewport(myElement, false) || checkAllSiblings(lastElement) ) && !hasElementHighlighted ){
					seletores[myElement.id].style.color = '#FA6300';
					hasElementHighlighted = true;
				} else {
					seletores[myElement.id].style.color = '#37404E';
				}
			}
		})
		
		}
		
		// HighLight Navigation Bar

		$("a[data-topico]").on('click', function(event) {
			if (this.hash !== "") {
			  event.preventDefault();
			  var hash = this.hash;
			  $('html, body').animate({ scrollTop: $(hash).offset().top
			  													}, 600, function() {
					window.location.hash = hash;
					var $target = $(hash);
					$target.focus();
					if ($target.is(":focus")) { 
						return false;
					} else {
						$target.attr('tabindex','-1'); 
						$target.focus(); 
					};
				});
			} 
		});

		$(".socialVerticalBar__link").each(function () {
			let URL = window.location.href;
			var anchor = $(this);
			anchor.attr("href", anchor.attr('href') + URL );

		})

		var idContent =  $("#idContentAssocia").val();
		$.get("/lumis/api/rest/associa/associacao/" + idContent, function(data){
			
			if(data.myArrayList.length <= 0) {
				$("#postsRelacionados").hide();
			}

			$.each(data.myArrayList, function (i, item) {

				$.each(item, function (x, itemInfo) {
					
					let urlImg = g_LumisRoot_href + "data/" + itemInfo.imageHref;
					
					let urlPage = itemInfo.href;
					if (urlPage.indexOf("lumPageId=") > 0) {
						urlPage = "/contenthyperlink.jsp?lumItemId=" + itemInfo.id;
					}

					let itemHtmlMobile =
						"<div class='swiper-slide'><div class='card_blog__popular'>" +
						"<a href='" + urlPage + "'>" +
						"<picture class='card_blog__popular__thumb'> <source media='(max-width: 1019px)' srcset='" + urlImg + "'>" + 
						"<source media='(min-width: 1020px)' srcset='" + urlImg + "'> <img alt='" + itemInfo.titulo + "' src='" + urlImg + "'>" +
						"</picture>" + 
						"</a>" +
						"<div class='card_blog__popular__content'><p class='card_blog__popular__subtitle tp1--caption tp1-desk--body1'>" +
						itemInfo.dataPublicacao + "<br><a class='link' href='/blog/" + itemInfo.linkCategoria + "'> " + itemInfo.categoria + "</a></p>" +
						"<a href='" + urlPage  + "' style='-webkit-box-orient: vertical;' class='card_blog__popular__title tp1--headline5 link'>" +
						itemInfo.titulo + "</a> <a class='tp1--link tp1-desk--link view__more link' href='" + urlPage + "'>Ler Mais </a></div></div></div>";
					
					let itemHtmlDesktop =
						"<div class='card_blog__popular grid-3 grid-xl-6'>" +
						"<a href='" + urlPage + "'>" +
						"<picture class='card_blog__popular__thumb'>" +
						"<source media='(max-width: 1019px)' srcset='" + urlImg + "'><source media='(min-width: 1020px)' srcset='" + urlImg + "'>" +
						"<img alt='" + itemInfo.titulo + "' src='" + urlImg + "'></picture>" +
						"</a>" +
						"<div class='card_blog__popular__content'>" +
						"<p class='card_blog__popular__subtitle tp1--caption tp1-desk--body1'>" + itemInfo.dataPublicacao + " | " +                                    
						"<a class='link' href='/blog/" + itemInfo.linkCategoria + "'>" + itemInfo.categoria + "</a></p>" +
						"<a href='" + urlPage + "' class='card_blog__popular__title tp1--display tp1-desk--headline4 link'>" + itemInfo.titulo + "</a>" +
						"<a class='tp1--link tp1-desk--link view__more link' style='-webkit-box-orient: vertical;' href='" + urlPage + "'>Ler Mais </a>" +
						"</div></div>";
				
				$("#postsRelacionadosMobile").append(itemHtmlMobile);
				$("#postsRelacionadosDesktop").append(itemHtmlDesktop);
				});
				
			});

		}).fail(function(data){
			console.log("Falha requisição GET associa");
		})
		
		$("#newsletter__form").validate({
			rules:{
				nome:{
					required: true,
					regex: /^([a-zA-Z ]*?)\s+([a-zA-Z]*)$/i
				},                
				email:{
					required: true,
					regex: /^\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b$/i
				},
			},     
			messages: {         
				required: "Você precisa preencher esse campo.",

				nome:{
					regex:"Você precisa preencher o nome completo"
				},
				email:{
					email:"O e-mail está certo? deve ser no formato @dominio.com.br",
					regex:"O e-mail está certo? deve ser no formato @dominio.com.br"
				}
			},
			submitHandler: function(form, event) {
				event.preventDefault();
				formLoading(form, true);

				var requestJsonObject = {"entity": JSON.stringify({
					"nome"			:  $("#nome").val(),
					"email"			:  $('#email').val(),
					"token"			: grecaptcha.getResponse(clientId).toString(),
					"cpf"			: null,
					"celular"		: null,
					"telefoneFixo"	: null,
					"origem"		: null
				})};

				$.ajax({
					type: "POST",
					dataType: "json",
					url: g_LumisRoot_href + "api/blog/formulario/newsletter",
					data: requestJsonObject,
					success: function(data){
						form.reset();

						$(form).find(".floatingLabel").removeClass("floatingLabel");
						$(form).find(".textarea__count__target").html("0");
						
						if(data.message && data.message.toString() && data.message.toString().toLowerCase().includes("sucesso")) {
							$("#modal-text-success").html(data.message);
							$(".modal--sucess").addClass("modal--show");
							var currentPageInfo = BMGDatalayer.pageInfo.name;
							BMGDatalayer.pageInfo.name = currentPageInfo + "|newsletter";
							Object.assign(BMGDatalayer, {"formInfo": {"title": "Newsletter - Artigo"}});
							document.dispatchEvent(new CustomEvent("lead_event", { detail: BMGDatalayer }));
						} else {
							$("#modal-text-error").html(data.message);
							$(".modal--error").addClass("modal--show");
						}
						
						formLoading(form, false);
					}
				}).fail(function(jqXHR){
					$(".modal--error").addClass("modal--show");
					formLoading(form, false);
					var currentPageInfo = BMGDatalayer.pageInfo.name;
					BMGDatalayer.pageInfo.name = currentPageInfo + "|newsletter";
					Object.assign(BMGDatalayer, {"error_system": jqXHR.status.toString()});
					document.dispatchEvent(new CustomEvent("error_event", { detail: BMGDatalayer }));
				});

			}
		});
		
		// Exibe o Banner cadastrado de acordo com a categoria do Post
		fetch(g_LumisRoot_href + "lumis/api/rest/banners-blog/lumgetdata/details?lumMaxRows=-1&lumReturnFields=id,titulo,introducao,imagem,categorization,url")
			.then(response => response.json())
			.then(data => {
				let $bannerPost = $('#bannerPost');
			    let banner = data.rows.find( row => {
					return row.categorization.serviceInstances[0] ? row.categorization.serviceInstances[0].serviceInstance.terms[0].term.name === $bannerPost.data("categoria") : null;
				});	
			
				if(banner && banner.imagem.href) {
					let introducao = banner.introducao || "";
					let imgAlt = banner.imagem.mediaLegend || "";
					
					if(banner.url) {
						$bannerPost.append('<a href="' + banner.url + '" title="' + introducao + '"><img src="'+ banner.imagem.href +'" class="fill" alt="' + imgAlt + '" /></a>')
					} else {
						$bannerPost.append('<img src="'+ banner.imagem.href +'" class="fill" alt="' + imgAlt + '" />')
					}
				}
			})
			.catch(function(error) {
				console.log('There has been a problem with fetch operation: ' + error.message); 
			});
		
	}); // Fim do document.ready

	$("#vote").on("rated", (function(a) {
		var news_id = $("#post-id").val();
		var idContent =  $("#idContent").val();
		var star = $("#vote").rateit("value");
		let starMedia = '';

		enviavoto(idContent, star);

		$.get("/lumis/api/rest/blogvoto/voto/" + idContent, function(data){
			starMedia = data;
			console.log(starMedia);
		}).fail(function(data){
			console.log("Falha requisição GET voto");
		})

		$(".box-voto").addClass("esconde"), 
			$(".box-mensagem").removeClass("esconde"), 
			$(".mostra-media").on("click", (function() {
			$(".box-mensagem").addClass("esconde"),
				$(".box-media").removeClass("esconde"), 
				$(".box-media h5 .nota").html(starMedia), 
				$(".box-media h5 .total").html(a),
				$("#media").rateit("value", starMedia)
		}))

	}));

	function enviavoto(cod, voto){
		$.post("/lumis/api/rest/blogvoto/voto?votocontentid=" + cod + "&votonota=" + voto, function(data){

		}).fail(function(data){
			console.log("Falha requisição POST voto");
		})
	};
</script>