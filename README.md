# Learning project for trying almost everything without dependencies (like spring security...etc) - created in 2017 - you should not use! 

# API (like front controller pattern)

Write an entity an you have full controll with rest - this is a webshop backend engine

Generic controller -> generic service -> generic repo

Only available for GET:
--

Endpoint: <domain> /secure or /sandbox

secure: live
sandbox: test


Endpoint: /<entity>?<filters>

Filters:

	skip: 			- Integer (default 0)
	limit: 			- Integer (default 100, max 100)
	orderType: ASC or DESC	- String
	orderBy: field name 	- String
	field:

		name=Peter 	- String
		<like>name=ete 	- String
		name={[
		"Peter",
		"David"]}	- JSON (OR) - NOTE: You need to url encode this json section!
		isValid=true 	- boolean
		

	If you have an entity with joins:
		(Example: User(root) + -> Delivery + -> Address)

		user.delivery.address.street=Example street 1.	- String
		<like>user.delivery.address.street=ple stre	- String
		user.delivery.address.street={[
			"Example street 1.",
			"Example street 2.",
			"Example street 3."]} 			- JSON (OR) - NOTE: You need to url encode this json section!

If you dont want to use generic mode you can choose custom mode:

Generic controller -> virtual controller (custom and this is not a real controller) - > generic service or custom service - > generic repo (if generic service used) or custom repo (if custom service used)

	- If you use generic service you can use all filters!!

--
