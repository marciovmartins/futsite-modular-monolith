import React, {useState} from "react";
import {Link, Outlet, Route, Routes, useLocation} from "react-router-dom";
import {GamedayList} from "../../components/gamedays/GamedayList";
import {GamedayNew} from "../../components/gamedays/GamedayNew";

export function GamedayPage() {
    const location = useLocation()
    const state = location.state
    const gamedaysLink = state.gamedaysLink
    const [creationLink, setCreationLink] = useState()

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
                />
            }/>
            <Route path="new" element={
                <GamedayNew
                    creationLink={creationLink}
                />
            }/>
        </Routes>
    </div>
}