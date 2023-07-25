import React, {useState} from "react";
import {handleChange} from "../../api/handleChange";
import {v4 as uuidv4} from 'uuid';

export function PlayerNew(
    {creationUrl, toPlayerList}
) {
    const [formData, setFormData] = useState({
        name: '',
    })

    const handleSubmit = (event) => {
        event.preventDefault()
        const playerId = uuidv4()
        submitAmateurSoccerGroupPlayer(creationUrl, playerId).then(response => {
            const userCorePlayerCreationUrl = response._links["set-player-user-data"].href
            submitUserCorePlayer(userCorePlayerCreationUrl, formData).then(console.log)
        })
    }

    return <div>
        <h1>New Player</h1>

        <form onSubmit={handleSubmit}>
            <div className="form-group row">
                <label htmlFor="playerName" className="col-sm-2 col-form-label">Name</label>
                <div className="col-sm-5">
                    <input type="text"
                           className="form-control"
                           name="name"
                           id="playerName"
                           value={formData.name}
                           onChange={handleChange(setFormData)}
                    />
                </div>
            </div>
            <button type="submit" className="btn btn-primary">Submit</button>
        </form>
    </div>
}

function submitAmateurSoccerGroupPlayer(link, playerId = uuidv4()) {
    return fetch(link, {
        method: 'POST',
        headers: {
            "Accept": "application/hal+json",
            "Content-Type": "application/json"
        },
        mode: "cors",
        body: JSON.stringify({playerId: playerId})
    })
        .then(response => response.json())
}

function submitUserCorePlayer(link, formData) {
    return fetch(link, {
        method: 'PUT',
        headers: {
            "Accept": "application/hal+json",
            "Content-Type": "application/json"
        },
        mode: "cors",
        body: JSON.stringify({name: formData.name})
    })
        .then(response => response.json())
}