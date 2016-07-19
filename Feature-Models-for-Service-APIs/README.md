# Feature Models for Service APIs
A JAVA library for describing and consuming RESTful APIs using Feature Models.

Representing RESTful APIs with Feature Models:
Software elements:
	Service
		Android service: IPC
		REST service: HTTP transaction
			REST request:
				Method
				Uri:
					Schema
					Host
					Path
					[Query parameters]
					[Fragment]
				Headers
				Content
			REST response
				Status line
					HTTP-Version
					Status code
					Reason phrase
				Headers
				Content
