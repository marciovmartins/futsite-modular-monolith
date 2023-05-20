import React, {useState} from 'react'
import {v4 as uuidv4} from 'uuid';

export function AmateurSoccerGroupNew(
    {creationUrl, setCreatedAmateurSoccerGroupUrl}
) {
    const [formData, setFormData] = useState({
        name: ''
    })
    const handleChange = (event) => {
        const {name, value} = event.target
        setFormData((prevFormData) => ({...prevFormData, [name]: value}))
    }

    const handleSubmit = (event) => {
        event.preventDefault()
        submitAmateurSoccerGroup(creationUrl, formData)
            .then(data => {
                setCreatedAmateurSoccerGroupUrl(data?._links?.self?.href)
            })
    }

    return <div>
        <h1>Create Amateur Soccer Group</h1>
        <form onSubmit={handleSubmit}>
            <label htmlFor="name">
                Name:
                <input type="text" id="name" name="name" value={formData.name} onChange={handleChange}/>
            </label>
            <p>
                <button type='submit'>Submit</button>
            </p>
        </form>
    </div>
}

function submitAmateurSoccerGroup(link, formData) {
    return fetch(link, {
        method: 'POST',
        headers: {
            "Accept": "application/hal+json",
            "Content-Type": "application/json"
        },
        mode: "cors",
        body: JSON.stringify({...formData, amateurSoccerGroupId: uuidv4()})
    })
        .then(response => response.json())
}