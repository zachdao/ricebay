import React from 'react';
import { View } from '@adobe/react-spectrum';

export const UserProfile = ({ src }) => {
    if (src) {
        return <img src={src} alt="user profile" width="80%" height="80%" />;
    } else {
        return (
            <View width="80%" height="80%">
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="100%"
                    height="100%"
                    viewBox="0 0 193 192"
                >
                    <g
                        id="Group_72652"
                        data-name="Group 72652"
                        transform="translate(0 0)"
                    >
                        <g
                            id="Default_Avatar"
                            data-name="Default Avatar"
                            transform="translate(24.411 24.331)"
                        >
                            <circle
                                id="XMLID_5_"
                                cx="71.795"
                                cy="71.795"
                                r="71.795"
                                transform="translate(0 0.426)"
                                fill="#00d8af"
                            />
                            <path
                                id="XMLID_4_"
                                d="M1288.32,151.758a71.9,71.9,0,1,1,47.265-135.815"
                                transform="translate(-1239.859 -11.929)"
                                fill="#00a888"
                            />
                        </g>
                        <rect
                            id="Rectangle_137218"
                            data-name="Rectangle 137218"
                            width="193"
                            height="192"
                            transform="translate(0 0)"
                            fill="red"
                            opacity="0"
                        />
                    </g>
                </svg>
            </View>
        );
    }
};
