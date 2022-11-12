import React, { useState } from 'react';
import { Grid, View } from '@adobe/react-spectrum';
import { Header } from './header/Header';
import { Sidebar } from './sidebar/Sidebar';
import { Route, Routes } from 'react-router-dom';
import { Account } from './account/Account';

export const App = () => {
    const [showSidebar, setShowSidebar] = useState(false);

    return (
        <Grid
            areas={['header header', 'sidebar content', 'footer footer']}
            columns={['.5fr', '3fr']}
            rows={['size-1000', 'auto', 'size-1000']}
            height="100vh"
            backgroundColor="white"
        >
            <View backgroundColor="gray-300" gridArea="header">
                <Header
                    menuClicked={() => setShowSidebar((prev) => !prev)}
                ></Header>
            </View>
            {showSidebar && (
                <View gridArea="sidebar" height="100%">
                    <Sidebar dismiss={() => setShowSidebar(false)} />
                </View>
            )}
            <View
                backgroundColor="gray-100"
                gridColumnStart={showSidebar ? 'content' : 'sidebar'}
                gridColumnEnd="content"
            >
                <Routes>
                    <Route path="account*" element={<Account />} />
                </Routes>
            </View>
            <View backgroundColor="blue-700" gridArea="footer"></View>
        </Grid>
    );
};
