package contracts.person

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url $(consumer('persons'),
                producer("persons"))
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body("""
                [
                    {
                        "name": "NAME1"
                    },
                    {
                        "name": "NAME2"
                    },
                    {
                        "name": "NAME3"
                    }
                ]
        """)
    }
}

