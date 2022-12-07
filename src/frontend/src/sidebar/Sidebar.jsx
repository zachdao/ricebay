import React, { useCallback } from 'react';
import { Flex, Heading } from '@adobe/react-spectrum';
import styled, { keyframes } from 'styled-components';
import { useLocation, useNavigate } from 'react-router-dom';

export const Sidebar = ({ dismiss }) => {
    const navigate = useNavigate();
    const location = useLocation();
    console.log('location', location);

    const navigateAndDismiss = useCallback(
        (route) => {
            dismiss();
            navigate(route);
        },
        [dismiss, navigate],
    );

    return (
        <Wrapper>
            <Flex direction="column" width="100%">
                <MenuItem
                    onClick={() => navigateAndDismiss('/')}
                    active={location.pathname === '/'}
                >
                    <Heading level={3}>Home</Heading>
                </MenuItem>
                <MenuItem
                    onClick={() => navigateAndDismiss('/search')}
                    active={location.pathname === '/search'}
                >
                    <Heading level={3}>Search</Heading>
                </MenuItem>
                <MenuItem
                    onClick={() => navigateAndDismiss('/account')}
                    active={location.pathname.startsWith('/account')}
                >
                    <Heading level={3}>Account</Heading>
                </MenuItem>
                <MenuItem
                    onClick={() => navigateAndDismiss('/recentlyViewed')}
                    active={location.pathname === '/recentlyViewed'}
                >
                    <Heading level={3}>Recently Viewed</Heading>
                </MenuItem>
                <MenuItem
                    onClick={() => navigateAndDismiss('/myauctions')}
                    active={location.pathname === '/myauctions'}
                >
                    <Heading level={3}>My Auctions</Heading>
                </MenuItem>
                <MenuItem
                    onClick={() => navigateAndDismiss('/purchases')}
                    active={location.pathname === '/purchases'}
                >
                    <Heading level={3}>My Purchases</Heading>
                </MenuItem>
                <MenuItem
                    onClick={() => navigateAndDismiss('/myBids')}
                    active={location.pathname === '/myBids'}
                >
                    <Heading level={3}>My Active Bids</Heading>
                </MenuItem>
            </Flex>
        </Wrapper>
    );
};

const MenuItem = styled.div`
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: start;
    padding-left: 25px;
    cursor: pointer;
    &:hover {
        background-color: var(
            --spectrum-alias-background-color-gray-200,
            var(
                --spectrum-global-color-gray-200,
                var(--spectrum-semantic-gray-200-color-background)
            )
        ) !important;
    }
    background-color: ${(props) =>
        props.active === true ? '#095aba2e' : 'unset'};
`;

const SlideOpen = keyframes`
    from {
        transform: translateX(-100%)
    }
    
    to {
        transform: translateX(0%)
    }
`;

const Wrapper = styled.div`
    width: 95%;
    height: 100%;
    box-shadow: grey 2px 9px 11px 0px;
    animation: ${SlideOpen} 0.125s ease-in-out 1 alternate;
`;
