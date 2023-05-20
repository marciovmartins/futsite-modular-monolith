import React, {useEffect, useState} from "react";
import {Link, Outlet, Route, Routes, useLocation, useNavigate} from "react-router-dom";
import {GamedayList} from "../../components/gamedays/GamedayList";
import {GamedayNew} from "../../components/gamedays/GamedayNew";
import {GamedayView} from "../../components/gamedays/GamedayView";

export function GamedayPage() {
    const state = useLocation().state;
    const navigate = useNavigate()

    const [viewUrl, setViewUrl] = useState()
    const [creationUrl, setCreationUrl] = useState()
    const [amateurSoccerGroupUrl, setAmateurSoccerGroupUrl] = useState()

    const gamedaysUrl = (state && state.gamedaysLink) || window.sessionStorage.getItem("gamedaysLink")

    useEffect(() => {
        window.sessionStorage.setItem("gamedaysLink", gamedaysUrl)
    }, [gamedaysUrl])

    return <div>
        <nav>
            {amateurSoccerGroupUrl
                && <Link to="/amateurSoccerGroups/view"
                         state={{amateurSoccerGroupLink: amateurSoccerGroupUrl}}
                > | Amateur Soccer Group</Link>}

            {gamedaysUrl &&
                <Link to="/gamedays" state={{gamedaysLink: gamedaysUrl}}> | List</Link>}

            {creationUrl
                && <Link to="/gamedays/new"
                         state={{gamedaysLink: gamedaysUrl}}
                > | New</Link>}
        </nav>

        <Outlet/>
        <Routes>
            <Route index element={
                <GamedayList
                    url={gamedaysUrl}
                    setCreationUrl={setCreationUrl}
                    creationUrl={creationUrl}
                    setViewUrl={(link) => {
                        setViewUrl(link)
                        navigate("/gamedays/view")
                    }}
                    setAmateurSoccerGroupUrl={setAmateurSoccerGroupUrl}
                />
            }/>

            <Route path="view" element={
                <GamedayView
                    uri={viewUrl}
                />
            }/>

            <Route path="new" element={
                <GamedayNew
                    creationUrl={creationUrl}
                    setViewUrl={(link) => {
                        setViewUrl(link)
                        navigate("/gamedays/view")
                    }}
                />
            }/>
        </Routes>
    </div>
}