import React, {useContext, useState} from "react";
import {Outlet, Route, Routes, useNavigate} from "react-router-dom";
import {GamedayList} from "../../components/gamedays/GamedayList";
import {GamedayNew} from "../../components/gamedays/GamedayNew";
import {GamedayView} from "../../components/gamedays/GamedayView";
import {MenuContext} from "../App";
import {AmateurSoccerGroupMenu} from "../amateurSoccerGroups/AmateurSoccerGroupMenu";

export function GamedayPage() {
    const navigate = useNavigate()
    const menu = useContext(MenuContext)

    const [viewUrl, setViewUrl] = useState()
    const [creationUrl, setCreationUrl] = useState()

    return <div>
        <AmateurSoccerGroupMenu menu={menu}/>
        <Outlet/>
        <Routes>
            <Route index element={
                <GamedayList
                    url={menu.amateurSoccerGroup.gamedaysUrl.value}
                    setCreationUrl={setCreationUrl}
                    creationUrl={creationUrl}
                    setViewUrl={(link) => {
                        setViewUrl(link)
                        navigate("/gamedays/view")
                    }}
                    setAmateurSoccerGroupUrl={menu.amateurSoccerGroup.viewUrl.set}
                    urlToNewGameday={creationUrl && "/gamedays/new"}
                />
            }/>

            <Route path="view" element={
                <GamedayView
                    url={viewUrl}
                />
            }/>

            <Route path="new" element={
                <GamedayNew
                    creationUrl={creationUrl}
                    setViewUrl={(link) => {
                        setViewUrl(link)
                        navigate("/gamedays/view")
                    }}
                    amateurSoccerGroupUrl={menu.amateurSoccerGroup.viewUrl.value}
                />
            }/>
        </Routes>
    </div>
}