import React, {useEffect, useState} from "react";
import {Link, Outlet, Route, Routes, useLocation} from "react-router-dom";
import {GamedayList} from "../../components/gamedays/GamedayList";
import {GamedayNew} from "../../components/gamedays/GamedayNew";
import {GamedayView} from "../../components/gamedays/GamedayView";

export function GamedayPage() {
    const [viewLink, setViewLink] = useState()
    const [creationLink, setCreationLink] = useState()
    const gamedaysLink = window.sessionStorage.getItem("gamedaysLink") || useLocation().state.gamedaysLink

    useEffect(() => {
        window.sessionStorage.setItem("gamedaysLink", gamedaysLink)
    }, [gamedaysLink])

    return <div>
        <nav>
            <Link to="/gamedays" state={{gamedaysLink}}>List</Link>

            {creationLink
                && <Link to="/gamedays/new"
                         state={{gamedaysLink}}
                > | New</Link>}
        </nav>

        <Outlet/>
        <Routes>
            <Route index element={
                <GamedayList
                    uri={gamedaysLink}
                    setCreationLink={setCreationLink}
                    creationLink={creationLink}
                    setViewLink={setViewLink}
                />
            }/>

            <Route path="view" element={
                <GamedayView
                    uri={viewLink}
                />
            }/>

            <Route path="new" element={
                <GamedayNew
                    creationLink={creationLink}
                    setViewLink={setViewLink}
                />
            }/>
        </Routes>
    </div>
}