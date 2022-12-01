import React, { useCallback, useContext, useState } from 'react';
import {
    Flex,
    Grid,
    Text,
    Button,
    TextField,
    ComboBox,
    Item,
    TextArea,
    RangeCalendar,
    LabeledValue,
    NumberField,
    StatusLight,
    Badge,
} from '@adobe/react-spectrum';
import PublishCheck from '@spectrum-icons/workflow/PublishCheck';
import Image from '@spectrum-icons/workflow/Image';
import { CategoryTagGroup } from '../../category-tag-group/CategoryTagGroup';
import styled from 'styled-components';
import { getLocalTimeZone, today, CalendarDate } from '@internationalized/date';
import { usePostWithToast } from '../../http-query/use-post-with-toast';
import Alert from '@spectrum-icons/workflow/Alert';
import { useNavigate } from 'react-router-dom';
import { CategoriesContext } from '../../categories.context';
import Cancel from '@spectrum-icons/workflow/Cancel';
import User from '@spectrum-icons/workflow/User';
import Copy from '@spectrum-icons/workflow/Copy';

export const EditAuction = ({ auction }) => {
    // Set up text field data
    const [image, setImage] = useState(auction?.images?.[0] || '');
    const [title, setTitle] = useState(auction?.title || '');
    const [startingBid, setStartingBid] = useState(auction?.minimumBid || 1);
    const [bidIncrement, setBidIncrement] = useState(
        auction?.bidIncrement || undefined,
    );
    const [description, setDescription] = useState(auction?.description || '');
    const startDate = auction?.startDate && new Date(auction.startDate);
    const endDate = auction?.endDate && new Date(auction.endDate);
    let [range, setRange] = React.useState({
        start: auction?.startDate
            ? new CalendarDate(
                  startDate.getFullYear(),
                  startDate.getMonth(),
                  startDate.getDate(),
              )
            : today(getLocalTimeZone()),
        end: auction?.endDate
            ? new CalendarDate(
                  endDate.getFullYear(),
                  endDate.getMonth(),
                  endDate.getDate(),
              )
            : today(getLocalTimeZone()).add({ weeks: 1 }),
    });
    const [categories, setCategories] = useState(
        new Set(auction?.categories || []),
    );
    const [published, setPublished] = useState(auction?.published);
    const [error, setError] = useState({});
    const navigate = useNavigate();

    const categoryOptions = useContext(CategoriesContext);

    const undo = usePostWithToast(
        `/auctions/${auction?.id}`,
        auction,
        [auction],
        { message: 'Auction restored!' },
        { message: 'Failed to undo auction save!' },
    );

    const saveAuction = usePostWithToast(
        auction ? `/auctions/${auction.id}` : '/auctions',
        {
            ...auction,
            title,
            image,
            minimumBid: startingBid,
            bidIncrement,
            startDate: range.start.toString(),
            endDate: range.end.toString(),
            categories: [...categories],
            description,
            published,
        },
        [
            title,
            image,
            startingBid,
            bidIncrement,
            range,
            categories,
            description,
            published,
        ],
        {
            message: auction ? 'Auction updated!' : 'Auction created!',
            actionTitle: auction && 'UNDO',
            actionFn: auction && undo,
        },
        { message: 'Failed to save auction!' },
        (appResponse) =>
            !auction && navigate(`/auction/${appResponse.data}/edit`),
        (axiosError) => setError(axiosError?.response?.data),
    );

    const copyAuction = usePostWithToast(
        `/auctions`,
        {
            ...auction,
            id: undefined,
            startDate: range.start.toString(),
            endDate: range.end.toString(),
        },
        [auction],
        { message: 'Auction Copied' },
        { message: 'Failed to Copy auction' },
        (appResponse) => navigate(`/auction/${appResponse.data}/edit`),
    );

    const isValid = useCallback(() => {
        const validTitle = title.length > 0;
        const validCategory = categories.size > 0;
        const validStartingBid = startingBid > 0;
        const validBidIncrement =
            bidIncrement !== undefined ? bidIncrement > 0 : true;
        const validDescription = description.length > 0;
        return (
            validTitle &&
            validCategory &&
            validStartingBid &&
            validBidIncrement &&
            validDescription
        );
    }, [title, categories, startingBid, bidIncrement, description]);

    const getLastBid = (bids) => {
        if (!bids) {
            return undefined;
        }

        return bids[bids.length - 1];
    };

    const isTerminal =
        auction?.published === false && auction?.bids?.length > 0;

    return (
        <Grid
            height="calc(100% - 40px)"
            maxWidth="960px"
            areas={['status save', 'image details', 'description description']}
            columns={['3fr', '2fr']}
            rows={['auto', 'auto', '1fr']}
            columnGap="size-200"
            rowGap="size-150"
        >
            <Flex
                gridArea="status"
                direction="row"
                alignItems="center"
                justifyContent="start"
            >
                {auction &&
                    published === false &&
                    auction.bids?.length === 0 && (
                        <RelistAuction onPress={() => setPublished(true)} />
                    )}
                {auction &&
                    auction.published === false &&
                    auction.bids?.length > 0 && (
                        <CopyAuction onPress={() => copyAuction()} />
                    )}
                {auction && auction.published === true && (
                    <CancelAuction
                        isDisabled={auction?.bids?.length > 0}
                        onPress={() => {
                            saveAuction({ published: false });
                        }}
                    />
                )}
                {auction && (
                    <AuctionState
                        isPublished={auction?.published}
                        lastBid={getLastBid(auction?.bids)}
                        isRelisted={
                            auction?.published === false && published === true
                        }
                    />
                )}
            </Flex>
            <Flex
                direction="row"
                space="size-100"
                justifyContent="right"
                gridArea="save"
            >
                <Button variant="negative" onPress={() => navigate(-1)}>
                    <Text>Cancel</Text>
                </Button>
                <Button
                    variant="primary"
                    marginStart="size-100"
                    onPress={() => saveAuction()}
                    isDisabled={!isValid() || isTerminal}
                >
                    <Text>Save</Text>
                </Button>
            </Flex>
            <ImageUploadWrapper onClick={() => console.log('click')}>
                <Image size="XXL" />
                <Text>Upload Image</Text>
            </ImageUploadWrapper>
            <Grid
                gridArea="details"
                columns={['2fr', '2fr']}
                autoRows="min-content"
                gap="size-100"
                height="100%"
            >
                <TextField
                    label="Auction Title"
                    value={title}
                    onChange={setTitle}
                    width="100%"
                    gridColumnStart="1"
                    gridColumnEnd="3"
                    isDisabled={isTerminal}
                />
                {!auction && (
                    <ComboBox
                        label="Add Category"
                        items={categoryOptions.map((opt) => ({ name: opt }))}
                        onSelectionChange={(selected) =>
                            setCategories(
                                (prev) => new Set([...prev, selected]),
                            )
                        }
                        isDisabled={isTerminal}
                    >
                        {(item) => <Item key={item.name}>{item.name}</Item>}
                    </ComboBox>
                )}
                <Flex
                    direction="column"
                    alignItems="start"
                    gap="size-100"
                    height="min-content"
                    width="100%"
                    gridColumnStart={auction ? '1' : undefined}
                    gridColumnEnd={auction ? '3' : undefined}
                >
                    <LabeledValue label="Categories" />
                    {categories.size ? (
                        <CategoryTagGroup
                            categories={categories}
                            onRemove={
                                auction
                                    ? undefined
                                    : (category) =>
                                          setCategories(
                                              (prev) =>
                                                  new Set(
                                                      Array.from(prev).filter(
                                                          (el) =>
                                                              el !== category,
                                                      ),
                                                  ),
                                          )
                            }
                        />
                    ) : (
                        <Warning>
                            <Alert color="negative" />
                            <Text>Missing Categories</Text>
                        </Warning>
                    )}
                </Flex>
                <Flex
                    direction="column"
                    gridColumnStart="1"
                    gridColumnEnd="3"
                    justifyContent="start"
                    alignItems="start"
                    height="min-content"
                >
                    <LabeledValue
                        label={`Auction Dates: ${range.start.toString()} to ${range.end.toString()}`}
                    />
                    <RangeCalendar
                        alignSelf="center"
                        aria-label="auction dates"
                        value={range}
                        onChange={setRange}
                        isDisabled={isTerminal}
                    />
                </Flex>

                <NumberField
                    label="Starting Bid"
                    value={startingBid}
                    onChange={setStartingBid}
                    isRequired
                    hideStepper
                    minValue={0}
                    validationState={error?.minimumBid ? 'invalid' : undefined}
                    errorMessage={error?.minimumBid}
                    formatOptions={{
                        style: 'currency',
                        currency: 'USD',
                        currencyDisplay: 'symbol',
                    }}
                    width="100%"
                    isDisabled={isTerminal}
                />
                <NumberField
                    minValue={0}
                    label="Bid Increment"
                    value={bidIncrement}
                    onChange={setBidIncrement}
                    hideStepper
                    validationState={
                        error?.bidIncrement ? 'invalid' : undefined
                    }
                    errorMessage={error?.bidIncrement}
                    formatOptions={{
                        style: 'currency',
                        currency: 'USD',
                        currencyDisplay: 'symbol',
                    }}
                    width="100%"
                    isDisabled={isTerminal}
                />
            </Grid>
            <TextArea
                label="Auction Description"
                gridArea="description"
                width="100%"
                value={description}
                onChange={setDescription}
                isDisabled={isTerminal}
            />
        </Grid>
    );
};
const ImageUploadWrapper = styled.div`
    grid-area: image;
    width: 100%;
    height: 100%;
    border-width: 2px;
    border-color: grey;
    border-radius: 5px;
    border-style: solid;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    user-select: none;
`;

const Warning = styled.div`
    display: flex;
    width: 100%;
    align-items: center;
    justify-content: space-around;
    font-size: 90%;
    font-weight: bold;
    color: var(
        --spectrum-button-warning-text-color,
        var(--spectrum-semantic-negative-color-text-small)
    );
`;

const RelistAuction = ({ onPress }) => {
    return (
        <Button variant="primary" maxWidth="max-content" onPress={onPress}>
            <PublishCheck />
            <Text>Re-list</Text>
        </Button>
    );
};

const CancelAuction = ({ onPress, isDisabled }) => {
    return (
        <Button
            variant="primary"
            maxWidth="max-content"
            onPress={onPress}
            isDisabled={isDisabled}
        >
            <Cancel />
            <Text>Cancel Auction</Text>
        </Button>
    );
};

const CopyAuction = ({ onPress }) => {
    return (
        <Button variant="primary" maxWidth="max-content" onPress={onPress}>
            <Copy />
            <Text>Copy Auction</Text>
        </Button>
    );
};

const AuctionState = ({ isPublished, lastBid, isRelisted }) => {
    let variant = 'positive';
    let statusText = `Published ${!lastBid ? '(No Bids)' : ''}`;
    if (isRelisted) {
        variant = 'neutral';
        statusText = 'Unpublished (Save to Publish)';
    } else if (!isPublished && !lastBid) {
        variant = 'negative';
        statusText = 'Expired';
    } else if (!isPublished && lastBid) {
        variant = 'info';
        statusText = 'Sold';
    }
    return (
        <Flex direction="row" alignItems="center">
            <StatusLight variant={variant}>{statusText}</StatusLight>
            {!isPublished && lastBid && (
                <Badge variant="positive" marginStart="size-100">
                    <User />
                    <Text>Buyer: {lastBid.alias}</Text>
                </Badge>
            )}
        </Flex>
    );
};
