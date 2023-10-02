export function fetchGraphQL(query) {
    return fetch("http://localhost:8080/graphql", {
        method: 'POST',
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        mode: "cors",
        body: JSON.stringify({query})
    }).then(response => response.json())
}