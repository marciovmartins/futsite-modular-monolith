import React, {useContext, useState} from "react";
import {Outlet, Route, Routes, useNavigate} from "react-router-dom";
import {GamedayList} from "../../components/gamedays/GamedayList";
import {GamedayNew} from "../../components/gamedays/GamedayNew";
import {MenuContext} from "../App";
import {AmateurSoccerGroupMenu} from "../amateurSoccerGroups/AmateurSoccerGroupMenu";

export function GamedayPage() {
    const navigate = useNavigate()
    const menu = useContext(MenuContext)

    const [, setViewUrl] = useState()

    return <div>
        <AmateurSoccerGroupMenu
            menu={menu}
        />
        <div className={"m-3"}>
            <Outlet/>
        </div>
        <Routes>
            <Route index element={
                <GamedayList
                    url={menu.amateurSoccerGroup.gamedaysUrl.value}
                    creationRedirectWhenEmptyUrl={"/gamedays/new"}
                />
            }/>

            <Route path="new" element={
                <GamedayNew
                    creationUrl={menu.amateurSoccerGroup.gamedaysCreationUrl.value}
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